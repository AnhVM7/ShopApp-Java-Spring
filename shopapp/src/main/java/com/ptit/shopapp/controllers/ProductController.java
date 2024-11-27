package com.ptit.shopapp.controllers;

import com.github.javafaker.Faker;
import com.ptit.shopapp.components.LocalizationUtil;
import com.ptit.shopapp.dtos.ProductDTO;
import com.ptit.shopapp.dtos.ProductImageDTO;
import com.ptit.shopapp.models.Product;
import com.ptit.shopapp.models.ProductImage;
import com.ptit.shopapp.responses.ProductListResponse;
import com.ptit.shopapp.responses.ProductResponse;
import com.ptit.shopapp.services.IProductService;
import jakarta.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {
  private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
  private final IProductService productService;
  private final LocalizationUtil localizationUtil;
  @PostMapping("")
  public ResponseEntity<?> createProduct(
      @Valid @RequestBody ProductDTO productDTO,
                           BindingResult result
   ) {
    try {
      // save product
      if(result.hasErrors()){
        List<String> errorMessages = result.getFieldErrors()
            .stream()
            .map(FieldError::getDefaultMessage)
            .toList();
        return ResponseEntity.badRequest().body(errorMessages);
      }
      Product newProduct = productService.createProduct(productDTO);

      return ResponseEntity.ok(newProduct);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  @PostMapping(value = "uploads/{id}",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> uploadImages(
      @PathVariable("id") Long productId,
      @RequestParam("files") List<MultipartFile> files
  ){
    try {
      Product existingProduct = productService.getProductById(productId);
      files = files == null ? new ArrayList<MultipartFile>() : files;
      if(files.size() > ProductImage.MAXIMUM_IMAGES_PER_PRODUCT){
        return ResponseEntity.badRequest().body("you can only upload max 5 images");
      }
      List<ProductImage> productImageList = new ArrayList<ProductImage>();
      for(MultipartFile file : files){
        if(file.getSize()==0) {
          continue;
        }
        //kiem tra kich thuoc file va dinh dang
        if(file.getSize() > 10 * 1024 * 1024){
          return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("File is too large");
        }
        String contentType = file.getContentType();
        if(contentType == null || !contentType.startsWith("image/")) {
          return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
              .body("File must be an image");
        }

        // luu file va cap nhat thumbnail trong DTO
        String filename = storeFile(file);
        // luu vao doi tuong product trong database
        // luu vao bang product_images
        ProductImage productImage = productService.createProductImage(
            existingProduct.getId(),
            ProductImageDTO
                .builder()
                .imageUrl(filename)
                .build()
        );
        productImageList.add(productImage);
      }
      return ResponseEntity.ok().body(productImageList);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping("/images/{imageName}")
  public ResponseEntity<?> viewImage(@PathVariable String imageName) {
    try {
      java.nio.file.Path imagePath = Paths.get("uploads/"+imageName);
      UrlResource resource = new UrlResource(imagePath.toUri());

      if (resource.exists()) {
        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_JPEG)
            .body(resource);
      } else {
        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_JPEG)
            .body(new UrlResource(Paths.get("uploads/notfound.jpeg").toUri()));
        //return ResponseEntity.notFound().build();
      }
    } catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
  }

  private boolean isImageFile(MultipartFile file){
    String contentType = file.getContentType();
    return contentType != null && contentType.startsWith("image/");
  }

  private String storeFile(MultipartFile file) throws IOException {
    if(!isImageFile(file) || file.getOriginalFilename() == null){
      throw new IOException("invalid image file format");
    }
    // lay ra ten file ma nguoi ban da upload
    String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
    // them UUID vao truoc ten file de dam bao ten file la duy nhat
    String uniqueFilename = UUID.randomUUID().toString() + "_" + filename;
    // duong dan den thu muc ma minh da luu file
    Path uploadDir = Paths.get("uploads");
    if(!Files.exists(uploadDir)) {
      Files.createDirectories(uploadDir);
    }
    // duong dan day du den file
    Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
    Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
    return uniqueFilename;
  }

  @GetMapping("")
  public ResponseEntity<ProductListResponse> getAllProducts(
      @RequestParam(defaultValue = "") String keyword,
      @RequestParam(defaultValue = "0", name = "category_id") Long categoryId,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int limit

  ){
    // tao pageable tu thong tin trang va gioi han
    PageRequest pageRequest = PageRequest.of(page, limit,
        Sort.by("id").ascending());
    logger.info(String.format("keyword = %s, categoryId = %d, page = %d, limit = %d", keyword, categoryId, page, limit));
    Page<ProductResponse> productPage = productService.getAllProducts(keyword, categoryId, pageRequest);
    int totalPages = productPage.getTotalPages();
    List<ProductResponse> products = productPage.getContent();
    return ResponseEntity.ok(ProductListResponse
        .builder()
        .productResponseList(products)
        .totalPages(totalPages)
        .build());
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getProductById(@PathVariable Long id){
    try {
      Product existingProduct = productService.getProductById(id);
      return ResponseEntity.ok(ProductResponse.fromProduct(existingProduct));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteById(@PathVariable Long id){
    try {
      Product existingProduct = productService.getProductById(id);
      if(existingProduct != null){
        productService.deleteProduct(id);
      }
      return ResponseEntity.ok("delete successfully");
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

//  @PostMapping("/generateFakeProduct")
  private ResponseEntity<String> generateFakeProducts(){
    Faker faker = new Faker();
    for(int i = 0; i < 100000; i++){
      String productName = faker.commerce().productName();
      if(productService.existsByName(productName)){
        continue;
      }
      ProductDTO productDTO = ProductDTO
          .builder()
          .name(productName)
          .price((float)faker.number().numberBetween(1000, 10000000))
          .description(faker.lorem().sentence())
          .thumbnail("")
          .categoryId((long)faker.number().numberBetween(3, 6))
          .build();
      try {
        productService.createProduct(productDTO);
      } catch (Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
      }
    }
    return ResponseEntity.ok("fake product created successfully");
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateProduct(
      @PathVariable Long id,
      @RequestBody ProductDTO productDTO
  ){
    try {
      Product updatedProduct = productService.updateProduct(id, productDTO);
      return ResponseEntity.ok(updatedProduct);
    } catch (Exception e){
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  @GetMapping("/by-ids")
  public ResponseEntity<?> getProductsByIds(@RequestParam("ids") String ids){
    try {
      // tach chuoi ids thanh 1 mang cac so nguyen
      List<Long> productIds = Arrays.stream(ids.split(","))
          .map(Long::parseLong)
          .collect(Collectors.toList());
      List<Product> products = productService.findProductsByIds(productIds);
      return ResponseEntity.ok(products);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}

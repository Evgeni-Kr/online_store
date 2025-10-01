package org.example.internet_shop.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="image")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @Column(name="imgName")
    private String name;
    @Column(name="originalFileName")
    private String originalFileName;
    @Column(name="imgType")
    private String contentType;
    @Column(name="imgSize")
    private Long size;
    @Lob
    private byte[] bytes;
    @Column(name="isPreviewImage")
    private boolean isPreviewImage;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "productId")
    private Product product;

    public void setPreviewImage(boolean isPreviewImage) {
        this.isPreviewImage = isPreviewImage;
    }
}

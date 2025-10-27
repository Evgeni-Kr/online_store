package org.example.internet_shop.Entity;

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
    @Column(name="img_name")
    private String name;
    @Column(name="original_file_name")
    private String originalFileName;
    @Column(name="img_type")
    private String contentType;
    @Column(name="img_size")
    private Long size;
    @Lob
    private byte[] bytes;
    @Column(name="is_preview_image")
    private boolean isPreviewImage;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "product_id")
    private Product product;

    public void setPreviewImage(boolean isPreviewImage) {
        this.isPreviewImage = isPreviewImage;
    }
}

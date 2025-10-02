package vn.iotstar.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name="Products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message="Tên sản phẩm không được để trống")
    @Column(nullable=false, length=200)
    private String name;

    @Positive(message="Giá phải > 0")
    @Column(nullable=false)
    private double price;

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;

    // Getter & Setter
    public Long getId() {return id;}
    public void setId(Long id) {this.id=id;}
    public String getName() {return name;}
    public void setName(String name) {this.name=name;}
    public double getPrice() {return price;}
    public void setPrice(double price) {this.price=price;}
    public Category getCategory() {return category;}
    public void setCategory(Category category) {this.category=category;}
}

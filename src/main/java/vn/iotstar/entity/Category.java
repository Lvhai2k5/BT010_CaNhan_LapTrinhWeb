package vn.iotstar.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name="Categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message="Tên category không được để trống")
    @Column(nullable=false, length=100)
    private String name;

    @OneToMany(mappedBy="category", cascade=CascadeType.ALL)
    private List<Product> products;

    // Getter & Setter
    public Long getId() {return id;}
    public void setId(Long id) {this.id=id;}
    public String getName() {return name;}
    public void setName(String name) {this.name=name;}
    public List<Product> getProducts() {return products;}
    public void setProducts(List<Product> products) {this.products=products;}
}

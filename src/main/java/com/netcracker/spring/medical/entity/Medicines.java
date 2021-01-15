package com.netcracker.spring.medical.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "medicines", schema = "medicine")
public class Medicines {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "treatment_medicines",
            joinColumns = { @JoinColumn(name = "medicines_id") },
            inverseJoinColumns = { @JoinColumn(name = "treatment_id") })
    private List<Treatment> treatments;

    public List<Treatment> getTreatments() {
        return treatments;
    }

    public void setTreatments(List<Treatment> treatments) {
        this.treatments = treatments;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

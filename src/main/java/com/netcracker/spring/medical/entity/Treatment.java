package com.netcracker.spring.medical.entity;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "treatment", schema = "medicine")
public class Treatment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    private String description;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "treatment_medicines",
            joinColumns = { @JoinColumn(name = "treatment_id") },
            inverseJoinColumns = { @JoinColumn(name = "medicines_id") })
    private List<Medicines> medicines;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "treatment_therapy",
            joinColumns = { @JoinColumn(name = "treatment_id") },
            inverseJoinColumns = { @JoinColumn(name = "therapy_id") })
    private List<Therapy> therapies;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Medicines> getMedicines() {
        return medicines;
    }

    public void setMedicines(List<Medicines> medicines) {
        this.medicines = medicines;
    }

    public void addMedicines(Medicines medicines) {
        this.medicines.add(medicines);
    }
    public void addTherapies(Therapy therapy) {
        this.therapies.add(therapy);
    }

    public List<Therapy> getTherapies() {
        return therapies;
    }

    public void setTherapies(List<Therapy> therapies) {
        this.therapies = therapies;
    }
}

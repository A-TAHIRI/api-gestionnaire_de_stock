package com.tahiri.gestiondestock.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import java.io.Serializable;
import java.time.Instant;
import java.util.Date;


@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AbstractEntity implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public   Integer id;

    @CreatedDate
    @Column(name ="createDate", nullable = false, updatable = false)
    private Instant createDate;

    @LastModifiedDate
    @Column(name="lastModifiedDate")
    private Instant lastModifiedDate;




}

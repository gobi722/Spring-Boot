package com.example.adminservice.Model;


import java.util.Date;

import com.example.adminservice.Service.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "orders")
public class Order implements BaseEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    @Column(name = "order_date")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Date order_date;

    @Temporal(TemporalType.DATE)
    @Column(name = "delivery_date")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Date delivery_date;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getOrderDate() {
        return order_date;
    }

    public void setOrderDate(Date orderDate) {
        this.order_date = order_date;
    }

    public Date getDeliveryDate() {
        return delivery_date;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.delivery_date = delivery_date;
    }
}

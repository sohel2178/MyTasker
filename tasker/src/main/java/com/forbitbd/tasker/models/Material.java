package com.forbitbd.tasker.models;

import java.io.Serializable;

public class Material implements Serializable {

    private ID _id;
    private double quantity;

    public Material() {
    }

    public ID get_id() {
        return _id;
    }

    public void set_id(ID _id) {
        this._id = _id;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public class ID implements Serializable {
        private String name;
        private String unit;

        public ID() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }
    }
}

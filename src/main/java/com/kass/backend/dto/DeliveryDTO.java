package com.kass.backend.dto;


    import com.kass.backend.models.EmpresaModel;
    import com.kass.backend.models.UserModel;

public class DeliveryDTO {

    private int deliveryRoleId;
    private UserModel user;
    private EmpresaModel empresa;

    public DeliveryDTO(int deliveryRoleId, UserModel user, EmpresaModel empresa) {
        this.deliveryRoleId = deliveryRoleId;
        this.user = user;
        this.empresa = empresa;
    }

    // Getters y setters
    public int getDeliveryRoleId() {
        return deliveryRoleId;
    }

    public void setDeliveryRoleId(int deliveryRoleId) {
        this.deliveryRoleId = deliveryRoleId;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public EmpresaModel getEmpresa() {
        return empresa;
    }

    public void setEmpresa(EmpresaModel empresa) {
        this.empresa = empresa;
    }

}

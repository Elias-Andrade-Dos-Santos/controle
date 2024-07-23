package stoque.controle.domain.entitys.userEntity;

public enum UserRole {
    SUPER("super"),
    ADMIN("admin"),
    USER("user");

    private String role;

    UserRole(String role){
        this.role = role;
    }

    public String getRoles(){
        return role;
    }
}

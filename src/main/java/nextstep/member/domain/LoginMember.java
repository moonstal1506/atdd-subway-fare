package nextstep.member.domain;

public class LoginMember {
    private String email;
    private Integer age;

    public LoginMember(String email, Integer age) {
        this.email = email;
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }
}

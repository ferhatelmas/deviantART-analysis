package user;

public class User {

  private String username="";
  private String age="";
  private String nationality="";
  private String gender="";
  private String role="";
  private String deviantSince="";

  //dynamic fields
  private String memberStatus="";
  private String noDeviations="";
  private String noComments="";
  private String noPageviews="";

  public String getAge() {
    return age;
  }

  public void setAge(String age) {
    this.age = age;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getMemberStatus() {
    return memberStatus;
  }

  public void setMemberStatus(String memberStatus) {
    this.memberStatus = memberStatus;
  }

  public String getNationality() {
    return nationality;
  }

  public void setNationality(String nationality) {
    this.nationality = nationality;
  }

  public String getNoComments() {
    return noComments;
  }

  public void setNoComments(String noComments) {
    this.noComments = noComments;
  }

  public String getNoDeviations() {
    return noDeviations;
  }

  public void setNoDeviations(String noDeviations) {
    this.noDeviations = noDeviations;
  }

  public String getNoPageviews() {
    return noPageviews;
  }

  public void setNoPageviews(String noPageviews) {
    this.noPageviews = noPageviews;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getDeviantSince() {
    return deviantSince;
  }

  public void setDeviantSince(String deviantSince) {
    this.deviantSince = deviantSince;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }
}
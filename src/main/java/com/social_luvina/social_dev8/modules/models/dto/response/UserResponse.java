package com.social_luvina.social_dev8.modules.models.dto.response;

import com.social_luvina.social_dev8.modules.models.entities.User;
import com.social_luvina.social_dev8.modules.models.enums.GenderEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
  private long id;
  private String firstName;
  private String lastName;
  private String address;
  private String job;
  private Date dateOfBirth;
  private GenderEnum gender;
  private String avatar;

  public UserResponse(User user) {
    this.id = user.getId();
    this.firstName = user.getFirstName();
    this.lastName = user.getLastName();
    this.address = user.getAddress();
    this.job = user.getJob();
    this.dateOfBirth = user.getDateOfBirth();
    this.gender = user.getGender();
    this.avatar = user.getAvatar();
  }
}

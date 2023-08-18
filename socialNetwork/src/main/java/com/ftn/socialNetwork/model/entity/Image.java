package com.ftn.socialNetwork.model.entity;

import javax.persistence.*;

import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "images")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String path;
    @Column
    private String type;
    @Column(length = 50000000)
    private byte[] picByte;

//    @OneToOne
//    private User user;

  public Image(String originalFilename, String contentType, byte[] bytes) {
  }

//   @ManyToOne
//    private Post post;

}

package com.kpi.project.model.post;

import com.kpi.project.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "POSTS")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "POST_ID")
    private Integer id;

    @Column(name = "URL", nullable = false)
    private String imageURL;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @Column(name = "COMMENT", nullable = false)
    private String comment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID")
    private User author;

    public Post(String imageURL, String description, String comment, User author) {
        this.imageURL = imageURL;
        this.description = description;
        this.comment = comment;
        this.author = author;
    }
}

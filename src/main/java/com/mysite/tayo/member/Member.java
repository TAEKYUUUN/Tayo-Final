package com.mysite.tayo.member;


import java.util.Date;
import java.util.List;

import com.mysite.tayo.chat.ChatMember;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "member_seq")
    @SequenceGenerator(name = "member_seq", sequenceName = "MEMBER_SEQ", allocationSize = 1)
    @Column(name = "member_idx")
    private Long memberIdx;
    
    @Column(name = "name", length=100)
    private String name;
    
    @Column(name = "id", length=100)
    private String id;
    
    @Column(name="password", length=100)
    private String password;
    
    @Column(name="phone", length=100)
    private String phone;
    
    @Column(name="company_idx")
    private Integer companyIdx;
    
    @Column(name="organization_idx")
    private Integer organizationIdx;
    
    @Column(name="rank_name", length=100)
    private String rankName;
    
    @Column(name="regist_date")
    private Date registDate;
    
    @Column(name="certification_number")
    private Integer certificationNumber;
    
    @Column(name="is_confirmed")
    private Integer isConfirmed;
    
    @Column(name="phone_company", length=100)
    private String phoneCompany;
    
    @Column(name="status_message", length=100)
    private String statusMessage;
    
    @Column(name="profile_image", length=100)
    private String profileImage;
    
    @Column(name="benefit_agree")
    private Integer benefitAgree;
    
    @Column(name="set_auto_logout")
    private Integer setAutoLogout;
    
    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<ChatMember> chatMemberList;
    
    
}

package com.mysite.tayo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "chat_contents_has_file")
public class ChatContentsHasFile {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "has_file_seq")
    @SequenceGenerator(name = "has_file_seq", sequenceName = "HAS_FILE_SEQ", allocationSize = 1)
    @Column(name = "has_file_idx")
    private Long hasFileIdx;

    @Column(name = "file_name", length = 200)
    private String fileName;

    @Column(name = "file_type", length = 100)
    private String fileType;

    @Column(name = "file_size", length = 100)
    private String fileSize;

}
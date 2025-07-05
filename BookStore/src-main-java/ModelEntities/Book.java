package com.example.demo.ModelEntitiesFinal;

import java.time.LocalDateTime;


import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.Table;

	@Entity
	@Table(name="BookDetails")
	
	public class Book {


	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private String name;

	    private String imageURL;

	    private String description;

	    private double price;

	    private String authorName;  // Added authorName field

	    
	    private String pdfURL;    // URL for PDF
	    private String audioURL;  // URL for Audio
	    private String videoURL;  // URL for Video
	    
	    
	    @CreatedDate
	    private LocalDateTime createdDate;

	    @LastModifiedDate
	    private LocalDateTime modifiedDate;

	    
	    
	    
	 // Default Constructor - No Parameters
	    public Book() {
	    }

	    // Constructor with parameters (Used when creating new book objects manually)
	    // Constructor with parameters (used when creating new book objects manually)
	    public Book(Long id, String name, String imageURL, String description, double price, String authorName, String pdfURL, String audioURL, String videoURL) {
	        this.id = id;
	        this.name = name;
	        this.imageURL = imageURL;
	        this.description = description;
	        this.price = price;
	        this.authorName = authorName;
	        this.pdfURL = pdfURL;
	        this.audioURL = audioURL;
	        this.videoURL = videoURL;
	    }
	    
	   
	   

		//Generate Getters and Setters
	    
	    
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getImageURL() {
			return imageURL;
		}

		public void setImageURL(String imageURL) {
			this.imageURL = imageURL;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public double getPrice() {
			return price;
		}

		public void setPrice(double price) {
			this.price = price;
		}

		public String getAuthorName() {
			return authorName;
		}

		public void setAuthorName(String authorName) {
			this.authorName = authorName;
		}
		
		public String getPdfURL() {
			return pdfURL;
		}

		public void setPdfURL(String pdfURL) {
			this.pdfURL = pdfURL;
		}

		public String getAudioURL() {
			return audioURL;
		}

		public void setAudioURL(String audioURL) {
			this.audioURL = audioURL;
		}

		public String getVideoURL() {
			return videoURL;
		}

		public void setVideoURL(String videoURL) {
			this.videoURL = videoURL;
		}

	

		public LocalDateTime getCreatedDate() {
			return createdDate;
		}

		public void setCreatedDate(LocalDateTime createdDate) {
			this.createdDate = createdDate;
		}

		public LocalDateTime getModifiedDate() {
			return modifiedDate;
		}

		public void setModifiedDate(LocalDateTime modifiedDate) {
			this.modifiedDate = modifiedDate;
		}

		


}

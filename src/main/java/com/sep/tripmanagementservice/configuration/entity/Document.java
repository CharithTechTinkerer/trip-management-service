package com.sep.tripmanagementservice.configuration.entity;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import com.sep.tripmanagementservice.configuration.dto.DocumentDto;
import com.sep.tripmanagementservice.configuration.enums.CommonStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "document")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "original_name", nullable = false)
	private String originalName;
	
	@Column(name = "unique_name", nullable = false)
	private String uniqueName;
	
	@Column(name = "added_at")
	private LocalDateTime addedAt;
	
	@Column(name = "added_by")
	private Long addedBy;
	
	@Column(name = "removed_at")
	private LocalDateTime removedAt;
	
	@Column(name = "removed_by")
	private Long removedBy;
	
	@Enumerated(EnumType.STRING)
	private CommonStatus status;
	
	@ManyToMany(mappedBy = "documents")
	private List<Trip> tripList;
	
	
	public Document(String originalName, String uniqueName, LocalDateTime addedAt, Long addedBy, CommonStatus status){
		this.originalName = originalName;
		this.uniqueName = uniqueName;
		this.addedAt = addedAt;
		this.addedBy = addedBy;
		this.status = status;
	}
	
	public DocumentDto convertDocumentToDto() {
		DocumentDto res = new DocumentDto();
			res.setId(this.id);
			res.setOriginalName(this.originalName);
			res.setUniqueName(this.uniqueName);
			res.setStatus(this.status);
		return res;
	}

	
	
}

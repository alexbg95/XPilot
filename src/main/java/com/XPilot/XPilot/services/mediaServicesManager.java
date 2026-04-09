package com.XPilot.XPilot.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.XPilot.XPilot.models.media;
import com.XPilot.XPilot.repositories.mediaRepository;
import com.cloudinary.Cloudinary;

@Service
public class mediaServicesManager implements mediaServices {

    @Autowired
    private mediaRepository repository;

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public List<media> all() {
        return (List<media>) this.repository.findAll();
    }

    @Override
    public media find(long cod) {
        return this.repository.findById(cod).orElse(null);
    }

    @Override
    public media save(media mda) {
        return this.repository.save(mda);
    }

    @Override
    public media update(long cod, media mda) {
        media exist = this.find(cod);
        if (exist != null) {
            exist.setArtist(mda.getArtist());
            exist.setStyle(mda.getStyle());
            exist.setTags(mda.getTags());
            exist.setUrlm(mda.getUrlm());
            exist.setDatem(mda.getDatem());
            return this.save(exist);
        }
        return null;
    }

    @Override
    public void delete(long cod) {
        this.repository.deleteById(cod);
    }

    /**
     * Sube un archivo a Cloudinary y retorna la URL segura.
     */
    @SuppressWarnings("unchecked")
    public String uploadToCloudinary(MultipartFile file) throws Exception {
        Map<String, Object> result = cloudinary.uploader().upload(
            file.getBytes(),
            com.cloudinary.utils.ObjectUtils.asMap(
                "folder", "xpilot", "upload_preset", "ml_default",
                "resource_type", "auto"
            )
        );
        return (String) result.get("secure_url");
    }
}

package com.angelcantero.listme.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.firebase.cloud.StorageClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * <p><strong>FirebaseStorageService</strong></p>
 * <p>Servicio para manejar uploads de archivos a Firebase Storage.</p>
 *
 * @author Angel Cantero
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FirebaseStorageService {

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final String IMAGES_FOLDER = "images";
    private static final String[] ALLOWED_TYPES = {"image/jpeg", "image/png", "image/webp"};

    /**
     * Sube una imagen a Firebase Storage.
     *
     * @param file archivo a subir
     * @param itemId ID del ítem
     * @return URL pública de la imagen
     * @throws IOException si hay error al subir
     * @throws IllegalArgumentException si el archivo no es válido
     */
    public String uploadImage(MultipartFile file, Long itemId) throws IOException {
        validateFile(file);

        String fileName = generateFileName(itemId);
        String path = IMAGES_FOLDER + "/" + itemId + "/" + fileName;

        StorageClient storageClient = StorageClient.getInstance();
        Storage storage = storageClient.bucket().getStorage();

        BlobInfo blobInfo = BlobInfo.newBuilder(storageClient.bucket().getName(), path)
                .setContentType(file.getContentType())
                .build();

        byte[] fileData = file.getBytes();
        storage.create(blobInfo, fileData);

        log.info("Imagen subida: {} para ítem {}", path, itemId);

        return generatePublicUrl(storageClient.bucket().getName(), path);
    }

    /**
     * Elimina una imagen de Firebase Storage.
     *
     * @param imagePath ruta de la imagen en Firebase
     */
    public void deleteImage(String imagePath) {
        try {
            StorageClient storageClient = StorageClient.getInstance();
            Blob blob = storageClient.bucket().getStorage()
                    .get(storageClient.bucket().getName(), imagePath);

            if (blob != null && blob.delete()) {
                log.info("Imagen eliminada: {}", imagePath);
            }
        } catch (Exception e) {
            log.error("Error al eliminar imagen: {}", imagePath, e);
        }
    }

    /**
     * Valida el archivo antes de subirlo.
     *
     * @param file archivo a validar
     * @throws IllegalArgumentException si el archivo no es válido
     */
    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("El archivo excede el tamaño máximo de 10MB");
        }

        String contentType = file.getContentType();
        boolean isAllowed = false;
        for (String allowed : ALLOWED_TYPES) {
            if (allowed.equals(contentType)) {
                isAllowed = true;
                break;
            }
        }

        if (!isAllowed) {
            throw new IllegalArgumentException("Tipo de archivo no permitido. Solo se aceptan JPEG, PNG y WebP");
        }
    }

    /**
     * Genera un nombre único para el archivo.
     *
     * @param itemId ID del ítem
     * @return nombre del archivo
     */
    private String generateFileName(Long itemId) {
        return UUID.randomUUID().toString();
    }

    /**
     * Genera la URL pública del archivo.
     *
     * @param bucketName nombre del bucket
     * @param path ruta del archivo
     * @return URL pública
     */
    private String generatePublicUrl(String bucketName, String path) {
        return String.format("https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media",
                bucketName, path.replace("/", "%2F"));
    }
}

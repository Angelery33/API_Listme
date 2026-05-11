package com.angelcantero.listme.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

/**
 * <p><strong>FirebaseConfig</strong></p>
 * <p>Configuración de Firebase Admin SDK para el proyecto.</p>
 *
 * @author Angel Cantero
 * @since 1.0.0
 */
@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            try (InputStream serviceAccount = getClass().getClassLoader()
                    .getResourceAsStream("firebase-adminsdk.json")) {

                if (serviceAccount == null) {
                    throw new IOException("No se encontró firebase-adminsdk.json en resources");
                }

                GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(credentials)
                        .setStorageBucket("listme-app.appspot.com")
                        .build();

                return FirebaseApp.initializeApp(options);
            }
        }
        return FirebaseApp.getInstance();
    }

    @Bean
    public StorageClient storageClient() throws IOException {
        firebaseApp();
        return StorageClient.getInstance();
    }
}

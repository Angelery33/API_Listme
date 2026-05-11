# Firebase Configuration

## Setup Instructions

1. Go to [Firebase Console](https://console.firebase.google.com)
2. Select your project (listme-app)
3. Click the ⚙️ icon → **Project Settings**
4. Go to **Service Accounts** tab
5. Click **Generate New Private Key**
6. Save the downloaded JSON file as `firebase-adminsdk.json` in this directory

## File Location

The `firebase-adminsdk.json` file should be placed in:
```
src/main/resources/firebase-adminsdk.json
```

This file is automatically loaded by `FirebaseConfig.java` during application startup.

## Security Note

⚠️ **NEVER commit this file to version control!**

The file is listed in `.gitignore` to prevent accidental exposure of credentials.

## Running the Application

Once `firebase-adminsdk.json` is in place, the application will:
- Initialize Firebase Admin SDK
- Enable image uploads to Firebase Storage via the `/api/v1/images/upload` endpoint
- All uploads are secured by JWT authentication

const { onDocumentDeleted } = require("firebase-functions/v2/firestore");
const { initializeApp } = require("firebase-admin/app");
const { getStorage } = require("firebase-admin/storage");

initializeApp();

exports.cleanupProcedure = onDocumentDeleted(
  "publishedProcedures/{docId}",
  async (event) => {
    const docId = event.params.docId;
    const bucket = getStorage().bucket();

    try {
      // everything for this procedure will live under procedures/<docId>/
      await bucket.deleteFiles({ prefix: `procedures/${docId}/` });
      console.log(`✔️ Storage cleaned for ${docId}`);
    } catch (err) {
      console.error(`Failed cleaning Storage for ${docId}`, err);
    }
  }
);

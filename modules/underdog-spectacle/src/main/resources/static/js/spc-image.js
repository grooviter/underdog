
class ImageBase64Loader {
    init(
        imageDivId,         // area to click
        imageFileFieldId,   // file field to make file dialog to show
        imageHtmlElementId, // element to show the loaded image
        imageValueFieldId,   // element to use to send image as base64,
        imageElementWrapperId
    ) {
        this.imageDiv = document.getElementById(imageDivId)
        this.imageFileField = document.getElementById(imageFileFieldId)
        this.imageHtmlElement = document.getElementById(imageHtmlElementId)
        this.imageValueField = document.getElementById(imageValueFieldId)
        this.imageElementWrapper = document.getElementById(imageElementWrapperId);

        this.imageDiv.addEventListener("click", () => {
            this.imageFileField.click();
        });

        this.imageFileField.addEventListener("change", (event) => {
            const file = event.target.files[0];

            if (!file) {
                return
            }
            const reader = new FileReader();
            reader.onload = () => {
                const imageAsURL = reader.result;
                const base64Only = imageAsURL.split(",")[1]
                // setting value as string to send with the form
                this.imageValueField.value = base64Only
                // showing image
                this.imageHtmlElement.src = imageAsURL
                // hide empty panel
                const emptyDiv = this.imageElementWrapper.querySelector(":scope > div.empty");
                const imageDiv = this.imageElementWrapper.querySelector(":scope > div:not(.empty)")

                if (emptyDiv) {
                    emptyDiv.classList.add("d-none");
                }

                if (imageDiv) {
                    imageDiv.classList.remove("d-none");
                }
            }
            reader.readAsDataURL(file)
        });
    }
}

export { ImageBase64Loader }
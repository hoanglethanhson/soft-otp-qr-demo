function setImage() {
    const barcodeText = document.getElementById("trans-code").value;
    console.log("code: " + barcodeText);
    document.getElementById("qr").setAttribute("src", "http://localhost:8080/qr-code-gen/gen?barcodeText=" + barcodeText + "");
}

document.getElementById("submit-code").addEventListener("click",  setImage);
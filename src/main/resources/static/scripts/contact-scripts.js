function openModalNewContact() {
    var newContactModal = document.getElementById("new-contact-modal");
    newContactModal.style.display = "block";
}

function openModalDeleteContact() {
    var deleteContactModal = document.getElementById("delete-contact-modal");
    deleteContactModal.style.display = "block";
}

function openModalEditContact(idContact, position, contactName, bankAccount, phone1, phone2, email) {
    var editContactModal = document.getElementById("edit-contact-modal");

    var idContactEle = document.getElementById("contact-edit-id");
    var positionEle = document.getElementById("contact-edit-position");
    var contactNameEle = document.getElementById("contact-edit-name");
    var bankAccountEle = document.getElementById("contact-edit-bank-account");
    var phone1Ele = document.getElementById("contact-edit-phone1");
    var phone2Ele = document.getElementById("contact-edit-phone2");
    var emailEle = document.getElementById("contact-edit-email");

    idContactEle.value = idContact;
    positionEle.value = position;
    contactNameEle.value = contactName;
    bankAccountEle.value = bankAccount;
    phone1Ele.value = phone1;
    phone2Ele.value = phone2;
    emailEle.value = email;

    editContactModal.style.display = "block";
}

document.addEventListener("click", function(event) {
    var newContactModal = document.getElementById("new-contact-modal");
    var deleteContactModal = document.getElementById("delete-contact-modal");
    var editContactModal = document.getElementById("edit-contact-modal");

    if (event.target == newContactModal) {
        newContactModal.style.display = "none";
    }
    if (event.target == deleteContactModal) {
        deleteContactModal.style.display = "none";
    }
    if (event.target == editContactModal) {
        editContactModal.style.display = "none";
    }
});

document.addEventListener("keydown", function(event) {
    var newContactModal = document.getElementById("new-contact-modal");
    var deleteContactModal = document.getElementById("delete-contact-modal");
    var editContactModal = document.getElementById("edit-contact-modal");

    if (event.keyCode == 27) {
        newContactModal.style.display = "none";
        deleteContactModal.style.display = "none";
        editContactModal.style.display = "none";
    }
});

function pressToClose() {
    var newContactModal = document.getElementById("new-contact-modal");
    var deleteContactModal = document.getElementById("delete-contact-modal");
    var editContactModal = document.getElementById("edit-contact-modal");

    newContactModal.style.display = "none";
    deleteContactModal.style.display = "none";
    editContactModal.style.display = "none";
}
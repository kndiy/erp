function openModalEditAddress(id, addressName, nameEn, nameVn, taxCodeAd, distance, type, representative, outsideCity) {
    var addressEditModal = document.getElementById('address-edit-modal');

    var idEle = document.getElementById('address-id');
    var addressEle = document.getElementById('address-address');
    var nameEnEle = document.getElementById('address-nameEn');
    var nameVnEle = document.getElementById('address-nameVn');
    var taxCodeEle = document.getElementById('address-tax-code');
    var distanceEle = document.getElementById('address-distance');
    var typeEle = document.getElementById('address-type');
    var representativeEle = document.getElementById('address-representative');
    var outsideCityEle = document.getElementById('address-outside-city');

    idEle.value = id;
    addressEle.value = addressName;
    nameEnEle.value = nameEn;
    nameVnEle.value = nameVn;
    taxCodeEle.value = taxCodeAd;
    distanceEle.value = distance;
    typeEle.value = type;
    representativeEle.value = representative;
    outsideCityEle.checked = outsideCity == "true" ? true : false;

    addressEditModal.style.display = "block";

}

function openModalNewAddress() {
    var addressNewModal = document.getElementById('address-new-modal');
    addressNewModal.style.display = "block";
}

function openModalDeleteAddress() {
    var addressDeleteModal = document.getElementById('delete-address-modal');
    addressDeleteModal.style.display = 'block';
}

function pressToClose() {
    var addressEditModal = document.getElementById('address-edit-modal');
    var addressNewModal = document.getElementById('address-new-modal');
    var addressDeleteModal = document.getElementById('delete-address-modal');

    addressEditModal.style.display = "none";
    addressNewModal.style.display = 'none';
    addressDeleteModal.style.display = 'none';
}

document.addEventListener("click", function(event) {
    var addressEditModal = document.getElementById('address-edit-modal');
    var addressNewModal = document.getElementById('address-new-modal');
    var addressDeleteModal = document.getElementById('delete-address-modal');

    if (event.target == addressEditModal) {
        addressEditModal.style.display = "none";
    }
    if (event.target == addressNewModal) {
        addressNewModal.style.display = "none";
    }
    if (event.target == addressDeleteModal) {
        addressDeleteModal.style.display = "none";
    }

});

document.addEventListener("keydown", function(event) {
    var addressEditModal = document.getElementById('address-edit-modal');
    var addressNewModal = document.getElementById('address-new-modal');
    var addressDeleteModal = document.getElementById('delete-address-modal');

    if (event.keyCode == 27) {
        addressNewModal.style.display = 'none';
        addressEditModal.style.display = 'none';
        addressDeleteModal.style.display = "none";
    }
});


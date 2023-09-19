function goToErpSourceCode() {
    window.location = 'https://github.com/kndiy/erp';
}

function goToWebNovelCopierSourceCode() {
    window.location = 'https://github.com/kndiy/CopyWebNovel';
}

function scrollToFunction(destination) {
    let ele = document.getElementById(destination);
    ele.scrollIntoView({behavior: 'smooth', block: 'center'});
}

function enlargeImage(imageTN) {

    let modal = document.getElementById('image-modal');
    addClosingAction(modal);

    let imageOfModal = modal.getElementsByClassName('image')[0];
    imageOfModal.src = imageTN.src;

    modal.style.display = 'block';
}

function openPasswordReminder() {
    let modal = document.getElementById('password-reminder');
    addClosingAction(modal);

    modal.style.display = 'block';
}
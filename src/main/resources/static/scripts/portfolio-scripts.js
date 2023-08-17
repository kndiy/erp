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
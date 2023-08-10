function toClose(modal) {

    let closeButton = modal.getElementsByClassName('close-button')[0];

    window.addEventListener('keydown', function(event) {
        if (event.keyCode == 27) {
            modal.style.display = 'none';
        }
    });
    modal.addEventListener('click', function(event) {
        if (event.target == modal) {
            modal.style.display = 'none';
        }
    });
    closeButton.addEventListener('click', function(event) {
        if (event.target == closeButton) {
            modal.style.display = 'none';
        }
    });
}

function openNewEquivalentModal() {
    let modal = document.getElementById('new-equivalent-modal');
    toClose(modal);
    modal.style.display = 'block';

    let newInputArr = modal.getElementsByClassName('edit-input');
    let sourceArr = document.getElementsByClassName('source-input');

    for (let i = 0; i < newInputArr.length; i ++) {
        newInputArr[i].value = sourceArr[i].textContent;
    }
}

function openEditEquivalentModal(idEquivalent) {
    let modal = document.getElementById('edit-equivalent-modal');
    toClose(modal);
    modal.style.display = 'block';

    let newInputArr = modal.getElementsByClassName('edit-input');
    let newSelectArr = modal.getElementsByClassName('edit-select');
    let lastInputArr = modal.getElementsByClassName('edit-last');

    let sourceArr = document.getElementsByClassName('source-input');

    let i = 0;
    for (; i < newInputArr.length; i ++) {
        newInputArr[i].value = sourceArr[i].textContent;
    }

    for (let j = 0; j < newSelectArr[0].children.length; j ++) {
        if (newSelectArr[0].children[j].value == sourceArr[i].textContent) {
            newSelectArr[0].selectedIndex = j;
            i++;
            break;
        }
    }

    for (let j = 0; j < newSelectArr[1].children.length; j ++) {
        if (newSelectArr[1].children[j].value == sourceArr[i].textContent) {
            newSelectArr[1].selectedIndex = j;
            i++;
            break;
        }
    }

    lastInputArr[0].value = sourceArr[i].textContent;
    lastInputArr[1].value = idEquivalent;
}

function inputNewSourceUnit(curSelect) {
    if (curSelect[curSelect.selectedIndex].value == 'NewUnit') {
        document.getElementById('new-source-unit').style.display = 'block';
        document.getElementById('edit-source-unit').style.display = 'block';
    }
    else {
        document.getElementById('new-source-unit').style.display = 'none';
        document.getElementById('edit-source-unit').style.display = 'none';
    }
}

function inputNewEquivalentUnit(curSelect) {
    if (curSelect[curSelect.selectedIndex].value == 'NewUnit') {
        document.getElementById('new-equivalent-unit').style.display = 'block';
        document.getElementById('edit-equivalent-unit').style.display = 'block';
    }
    else {
        document.getElementById('new-equivalent-unit').style.display = 'none';
        document.getElementById('edit-equivalent-unit').style.display = 'none';
    }
}

function openDeleteEquivalentModal(idEquivalent) {
    let modal = document.getElementById('delete-equivalent-modal');
    modal.style.display = 'block';
    toClose(modal);

    modal.getElementsByClassName('modal-id-equivalent')[0].value = idEquivalent;


}
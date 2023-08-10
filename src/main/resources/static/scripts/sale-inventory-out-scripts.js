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


function openNewInventoryOutWindow() {
    let form = document.getElementById('add-inventory-out-form');

    let idSale = document.getElementById('temp-id-sale').textContent;
    let idArticle = document.getElementById('temp-id-article').textContent;
    let idContainer = document.getElementById('temp-id-container').textContent;
    let idLot = document.getElementById('temp-id-lot').textContent;

    let actionString = "/sales/" + curIdSale + "/articles/" + curIdSaleArticle + "/containers/" + curIdSaleContainer + "/lots/" + curIdSaleLot + "/inventories-out/";

    form.action = actionString;
    form.submit();
}

let lastInventoryOutCheckMark;
function onclickInventoryOut(curInventoryOut) {

    let hiddenButton = document.getElementById('inventory-out-grid-header').getElementsByClassName('hidden-button');
    let idInventoryOut = curInventoryOut.children[0].children[1].textContent;

    for (let i = 0; i < hiddenButton.length; i ++) {
        hiddenButton[i].style.display = 'inline';
    }

    let radioMark = curInventoryOut.getElementsByClassName('check-radio')[0];
    if (lastInventoryOutCheckMark != null && lastInventoryOutCheckMark != radioMark) {
            lastInventoryOutCheckMark.style.background = 'white';
        }
        radioMark.style.background = '#F9D997';
        lastInventoryOutCheckMark = radioMark;

    let modalIdInventoryOutArr = document.getElementsByClassName('modal-id-inventory-out');
    for (let i = 0; i < modalIdInventoryOutArr.length; i ++) {
        modalIdInventoryOutArr[i].value = idInventoryOut;
    }
}

function openDeleteInventoryOutModal() {
    let modal = document.getElementById('delete-inventory-out-modal');
    toClose(modal);

    modal.style.display = 'block';
}



function openNewSaleModal() {
    var modal = document.getElementById('new-sale-modal');
    var closeButton = modal.getElementsByClassName('close-button')[0];

    modal.style.display = 'block';

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

function openEditSaleModal() {
    var modal = document.getElementById('edit-sale-modal');
    var closeButton = modal.getElementsByClassName('close-button')[0];

    modal.style.display = 'block';

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

function openDeleteSaleModal() {
    var modal = document.getElementById('delete-sale-modal');
    var closeButton = modal.getElementsByClassName('close-button')[0];

    modal.style.display = 'block';

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

let curIdSale;
let curIdSaleArticle;
let curIdSaleContainer;
let curIdSaleLot;

var lastSaleCheckMark;
var lastArticleGrid;
function onclickSale(curSaleGrid) {

    var checkMark = curSaleGrid.getElementsByClassName('check-radio')[0];
    var hiddenButtonArr = document.getElementById('sale-grid-header').getElementsByClassName('hidden-button');
    var articleHiddenButton = document.getElementById('article-grid-header').getElementsByClassName('hidden-button');
    var containerHiddenButton = document.getElementById('container-grid-header').getElementsByClassName('hidden-button');
    var lotHiddenButton = document.getElementById('lot-grid-header').getElementsByClassName('hidden-button');
    var inventoryOutHiddenButtons = document.getElementById('inventory-out-grid-header').getElementsByClassName('hidden-button');

    var idSale = curSaleGrid.children[0].children[1].textContent;
    curIdSale = idSale;

    //CHANGE URL
    let sourceSite = window.location.href.split('/')[0];
    let curSection = 'sales';
    let url = sourceSite + "/" + curSection + "/" + curIdSale + "/";
    window.history.pushState({}, null, url);

    //set temp id sale
    document.getElementById('temp-id-sale').textContent = idSale;

    if (lastSaleCheckMark == null) {
        for (let i = 0; i < hiddenButtonArr.length; i ++) {
            hiddenButtonArr[i].style.display = 'inline';
        }
    }

    for (let i = 0; i < articleHiddenButton.length; i ++) {
        articleHiddenButton[i].style.display = 'none';
    }
    for (let i = 0; i < containerHiddenButton.length; i ++) {
        containerHiddenButton[i].style.display = 'none';
    }
    for (let i = 0; i < lotHiddenButton.length; i ++) {
        lotHiddenButton[i].style.display = 'none';
    }
    for (let i = 0; i < inventoryOutHiddenButtons.length; i ++) {
        inventoryOutHiddenButtons[i].style.display = 'none';
    }
    articleHiddenButton[0].style.display = 'inline';

    if (lastSaleCheckMark != null && lastSaleCheckMark != checkMark) {
        lastSaleCheckMark.style.background = 'white';
    }
    checkMark.style.background = '#F9D997';
    lastSaleCheckMark = checkMark;

    var sourceArr = curSaleGrid.getElementsByClassName('input-source');
    var checkBoxSourceArr = curSaleGrid.getElementsByClassName('check-box');

    var editInputArr = document.getElementById('edit-sale-modal').getElementsByClassName('input-edit');
    var editSelectArr = document.getElementById('edit-sale-modal').getElementsByClassName('select-edit');
    var editCheckBoxArr = document.getElementById('edit-sale-modal').getElementsByClassName('check-box');

    //check boxed
    for (let i = 0; i < checkBoxSourceArr.length; i ++) {
        editCheckBoxArr[i].checked = checkBoxSourceArr[i].checked;
    }

    //id
    editInputArr[0].value = sourceArr[0].textContent;
    //date
    var dateStringArr = sourceArr[1].textContent.split('/');
    dateStringArr[0] = '20' + dateStringArr[0];
    var dateString = dateStringArr.join('-');
    editInputArr[1].value = dateString;
    //orderName
    editInputArr[2].value = sourceArr[4].textContent;
    //orderBatch
    editInputArr[4].value = sourceArr[5].textContent;
    //note
    editInputArr[3].value = sourceArr[6].textContent;
    //company source
    for (let i = 0; i < editSelectArr[0].children.length; i++) {
        if (editSelectArr[0].children[i].value == parseInt(sourceArr[2].textContent)) {
            editSelectArr[0].selectedIndex = i;
            break;
        }
    }
    //customer
    for (let i = 0; i < editSelectArr[1].children.length; i++) {
        if (editSelectArr[1].children[i].value == parseInt(sourceArr[3].textContent)) {
            editSelectArr[1].selectedIndex = i;
            break;
        }
    }
    //placer
    for (let i = 0; i < editSelectArr[2].children.length; i++) {
        if (editSelectArr[2].children[i].value == parseInt(sourceArr[7].textContent)) {
            editSelectArr[2].selectedIndex = i;
            break;
        }
    }

    //delete input
    var deleteInput = document.getElementById('delete-sale-input');
    deleteInput.value = sourceArr[0].textContent;

    var articleIdSaleArr = document.getElementsByClassName('article-id-sale');
    for (let i = 0; i < articleIdSaleArr.length; i ++) {
        articleIdSaleArr[i].value = idSale;
    }

    //GET ARTICLE FROM ARTICLE MAP
    var articleGridArr = document.getElementsByClassName('id-sale-key');
    if (lastArticleGrid != null) {
        lastArticleGrid.style.display = 'none';
    }
    if (lastArticleCheckMark != null) {
            lastArticleCheckMark.style.background = 'white';
    }

    if (lastContainerGrid != null) {
        lastContainerGrid.style.display = 'none';
    }
    if (lastContainerCheckMark != null) {
        lastContainerCheckMark.style.background = 'white';
    }

    if (lastLotGrid != null) {
        lastLotGrid.style.display = 'none';
    }

    if (lastInventoryOutGrid != null) {
        lastInventoryOutGrid.style.display = 'none';
    }

    if (lastLotCheckMark != null) {
        lastLotCheckMark.style.background = 'white';
    }

    for (let i = 0; i < articleGridArr.length; i ++) {
        if (articleGridArr[i].children[0].textContent == idSale) {
            articleGridArr[i].style.display = 'block';
            lastArticleGrid = articleGridArr[i];
            break;
        }
    }



    //sale lot selects
        //destination select
    var destinationSelectArr = document.getElementsByClassName('modal-destination-select');
    for (let i = 0; i < destinationSelectArr.length; i ++) {
        var curSelect = destinationSelectArr[i];
        for (let j = 1; j < curSelect.children.length; j += 2) {
            var companyAbbr = curSelect.children[j].value;
            if (companyAbbr == curSaleGrid.getElementsByClassName('sale-abbreviation')[0].textContent) {
                destinationSelectArr[i].children[j + 1].style.display = 'block';
            }
            else {
                destinationSelectArr[i].children[j + 1].style.display = 'none';
            }
        }
    }
}

window.addEventListener('load', function() {
    var gridSaleArr = document.getElementsByClassName('grid-sale main');

     for (let i = 0; i < gridSaleArr.length; i++) {
        var doneDeliverEle = gridSaleArr[i].getElementsByClassName('check-box')[0];
        if (doneDeliverEle.checked == false) {
            gridSaleArr[i].children[0].style.border = '2px solid red';
        }
     }

     let curUrl = window.location.href.split("/");
     for (let i = 0; i < curUrl.length - 1; i ++) {
        switch (curUrl[i]) {
            case 'sales' :
                curIdSale = curUrl[i + 1];
                clickMap(curIdSale, 'grid-sale main');
                break;
            case 'articles' :
                curIdSaleArticle = curUrl[i + 1];
                clickMap(curIdSaleArticle, 'grid-sale-article main');
                break;
            case 'containers' :
                curIdSaleContainer = curUrl[i + 1];
                clickMap(curIdSaleContainer, 'grid-sale-container main');
                break;
            case 'lots' :
                curIdSaleLot = curUrl[i + 1];
                clickMap(curIdSaleLot, 'grid-sale-lot main');
                break;
        }
     }

});

function clickMap(id, className) {
    let map = document.getElementsByClassName(className);

    for (let i = 0; i < map.length; i ++) {
        if (map[i].getElementsByClassName('id')[0].textContent == id) {
            map[i].click();
        }
    }
}



function onchangeCustomerSelect(select) {
    let idCompany = select[select.selectedIndex].value;
    let placerSelect = document.getElementById('new-sale placer-select');

    for (let i = 1; i < placerSelect.children.length; i += 2) {
        if (placerSelect.children[i].value == idCompany) {
            placerSelect.children[i + 1].style.display = 'block';
        }
        else {
            placerSelect.children[i + 1].style.display = 'none';
        }
    }
}
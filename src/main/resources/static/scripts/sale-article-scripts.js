


function openNewArticleModal() {
    var modal = document.getElementById('new-article-modal');
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

function onchangeCategoryFilter(categoryEle) {
    var typeSelect = document.getElementById('new-article-select-item-type');
    var codeSelect = document.getElementById('new-article-select-item-code');

    typeSelect.selectedIndex = 0;
    codeSelect.selectedIndex = 0;

    var idCategory = categoryEle[categoryEle.selectedIndex].value;

    if (idCategory == 0) {
        for (let j = 2; j < typeSelect.children.length; j += 2) {
            typeSelect.children[j].style.display = 'block';
        }

        for (let j = 3; j < codeSelect.children.length; j += 3) {
            codeSelect.children[j].style.display = 'block';
        }
    }
    else {
        for (let j = 1; j < typeSelect.children.length; j += 2) {
            if (typeSelect.children[j].value != idCategory) {
                typeSelect.children[j + 1].style.display = 'none';
            }
            else {
                typeSelect.children[j + 1].style.display = 'block';
            }
        }

        for (let j = 1; j < codeSelect.children.length; j += 3) {
            if (codeSelect.children[j].value != idCategory) {
                codeSelect.children[j + 2].style.display = 'none';
            }
            else {
                codeSelect.children[j + 2].style.display = 'block';
            }
        }
    }
}

function onchangeTypeFilter(typeEle) {
    var codeSelect = document.getElementById('new-article-select-item-code');
    var categorySelect = document.getElementById('new-article-select-item-category');

    codeSelect.selectedIndex = 0;

    var idCategory = categorySelect[categorySelect.selectedIndex].value;
    var idType = typeEle[typeEle.selectedIndex].value;

    if (idType == 0 && idCategory == 0) {
        for (let j = 3; j < codeSelect.children.length; j += 3) {
            codeSelect.children[j].style.display = 'block';
        }
    }
    else if (idType != 0) {
        for (let j = 2; j < codeSelect.children.length; j += 3) {
            if (codeSelect.children[j].value != idType) {
                codeSelect.children[j + 1].style.display = 'none';
            }
            else {
                codeSelect.children[j + 1].style.display = 'block';
            }
        }
    }
    else if (idCategory != 0) {
        for (let j = 1; j < codeSelect.children.length; j += 3) {
            if (codeSelect.children[j].value != idCategory) {
                codeSelect.children[j + 2].style.display = 'none';
            }
            else {
                codeSelect.children[j + 2].style.display = 'block';
            }
        }
    }
}









function openEditArticleModal() {
    var modal = document.getElementById('edit-article-modal');
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

function onchangeEditCategoryFilter(categoryEle) {
    var typeSelect = document.getElementById('edit-article-select-item-type');
    var codeSelect = document.getElementById('edit-article-select-item-code');

    typeSelect.selectedIndex = 0;
    codeSelect.selectedIndex = 0;

    var idCategory = categoryEle[categoryEle.selectedIndex].value;

    if (idCategory == 0) {
        for (let j = 2; j < typeSelect.children.length; j += 2) {
            typeSelect.children[j].style.display = 'block';
        }

        for (let j = 3; j < codeSelect.children.length; j += 3) {
            codeSelect.children[j].style.display = 'block';
        }
    }
    else {
        for (let j = 1; j < typeSelect.children.length; j += 2) {
            if (typeSelect.children[j].value != idCategory) {
                typeSelect.children[j + 1].style.display = 'none';
            }
            else {
                typeSelect.children[j + 1].style.display = 'block';
            }
        }

        for (let j = 1; j < codeSelect.children.length; j += 3) {
            if (codeSelect.children[j].value != idCategory) {
                codeSelect.children[j + 2].style.display = 'none';
            }
            else {
                codeSelect.children[j + 2].style.display = 'block';
            }
        }
    }
}

function onchangeEditTypeFilter(typeEle) {
    var codeSelect = document.getElementById('edit-article-select-item-code');
    var categorySelect = document.getElementById('edit-article-select-item-category');

    codeSelect.selectedIndex = 0;

    var idCategory = categorySelect[categorySelect.selectedIndex].value;
    var idType = typeEle[typeEle.selectedIndex].value;

    if (idType == 0 && idCategory == 0) {
        for (let j = 3; j < codeSelect.children.length; j += 3) {
            codeSelect.children[j].style.display = 'block';
        }
    }
    else if (idType != 0) {
        for (let j = 2; j < codeSelect.children.length; j += 3) {
            if (codeSelect.children[j].value != idType) {
                codeSelect.children[j + 1].style.display = 'none';
            }
            else {
                codeSelect.children[j + 1].style.display = 'block';
            }
        }
    }
    else if (idCategory != 0) {
        for (let j = 1; j < codeSelect.children.length; j += 3) {
            if (codeSelect.children[j].value != idCategory) {
                codeSelect.children[j + 2].style.display = 'none';
            }
            else {
                codeSelect.children[j + 2].style.display = 'block';
            }
        }
    }
}

var lastContainerGrid;
var lastArticleCheckMark;
function onclickArticle(curArticleEle) {

    var idSaleArticle = curArticleEle.children[5].children[1].textContent;
    curIdSaleArticle = idSaleArticle;

    //CHANGE URL
    let sourceSite = window.location.href.split('/')[0];
    let curSection = 'articles';
    let url = sourceSite + "/sales/" + curIdSale + "/" + curSection + "/" + curIdSaleArticle + "/";
    window.history.pushState({}, null, url);

    //set temp id article
    document.getElementById('temp-id-article').textContent = idSaleArticle;

    document.getElementById('delete-article-input').value = idSaleArticle;

    const months = new Map([
          ['Jan', '01'],
          ['Feb', '02'],
          ['Mar', '03'],
          ['Apr', '04'],
          ['May', '05'],
          ['Jun', '06'],
          ['Jul', '07'],
          ['Aug', '08'],
          ['Sep', '09'],
          ['Oct', '10'],
          ['Nov', '11'],
          ['Dec', '12']
          ]);

    var editInputArr = document.getElementById('edit-article-modal').getElementsByClassName('edit-input');
    var editSelectArr = document.getElementById('edit-article-modal').getElementsByClassName('edit-select');
    var sourceInputArr = curArticleEle.getElementsByClassName('input-source');


    //idSaleArticle
    editInputArr[0].value = sourceInputArr[2].textContent;
    //ETD
    var dateStringArr = sourceInputArr[3].textContent.split('/');
    dateStringArr[1] = months.get(dateStringArr[1]);
    var dateString = dateStringArr.join('-');
    editInputArr[1].value = dateString;
    //itemCode
    for (let i = 3; i < editSelectArr[0].children.length; i += 3) {
        if (editSelectArr[0].children[i].textContent == sourceInputArr[0].textContent) {
            editSelectArr[0].selectedIndex = i;
            break;
        }
    }
    //allow surplus
    editInputArr[2].value = sourceInputArr[1].textContent;



    //hidden buttons
    var articleHiddenButtonArr = document.getElementById('article-grid-header').getElementsByClassName('hidden-button');
    var containerHiddenButton = document.getElementById('container-grid-header').getElementsByClassName('hidden-button');
    var lotHiddenButton = document.getElementById('lot-grid-header').getElementsByClassName('hidden-button');
    var inventoryOutHiddenButtons = document.getElementById('inventory-out-grid-header').getElementsByClassName('hidden-button');

    for (let i = 0; i < articleHiddenButtonArr.length; i ++) {
        if (i == 0) {
            articleHiddenButtonArr[i].style.display = 'none';
        }
        else {
            articleHiddenButtonArr[i].style.display = 'inline';
        }
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
    containerHiddenButton[0].style.display = 'inline';

    //checkMark
    var radioMark = curArticleEle.children[5].children[0];
    if (lastArticleCheckMark != null && lastArticleCheckMark != radioMark) {
        lastArticleCheckMark.style.background = 'white';
    }
    radioMark.style.background = '#F9D997';
    lastArticleCheckMark = radioMark;

    //filter Container grid
    var containerGridArr = document.getElementsByClassName('id-article-key');
    if (lastContainerGrid != null) {
        lastContainerGrid.style.display = 'none';
    }
    if (lastContainerCheckMark != null) {
        lastContainerCheckMark.style.background = 'white';
    }

    if (lastLotGrid != null) {
        lastLotGrid.style.display = 'none';
    }
    if (lastLotCheckMark != null) {
        lastLotCheckMark.style.background = 'white';
    }

    if (lastInventoryOutGrid != null) {
        lastInventoryOutGrid.style.display = 'none';
    }

    for (let i = 0; i < containerGridArr.length; i ++) {
        if (containerGridArr[i].children[0].textContent == idSaleArticle) {
            containerGridArr[i].style.display = 'block';
            lastContainerGrid = containerGridArr[i];
            break;
        }
    }



    //Set idSaleArticle in Container Modals
    var modalIdSaleArticle = document.getElementsByClassName('modal-id-article');
    for (let i = 0; i < modalIdSaleArticle.length; i ++) {
        modalIdSaleArticle[i].value = idSaleArticle;
    }

    document.getElementById('temp-allowed-surplus').textContent = curArticleEle.getElementsByClassName('allowed-surplus')[0].textContent;


}

function openDeleteArticleModal() {
    var modal = document.getElementById('delete-article-modal');
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
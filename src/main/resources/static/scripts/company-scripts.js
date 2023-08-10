
function openModalF() {
    var modal = document.getElementById("myModal");
    modal.style.display = "block";
}

function pressToClose() {
    var modal = document.getElementById("myModal");
    modal.style.display = "none";

    var modalEdit = document.getElementById('modal-edit');
    modalEdit.style.display = 'none';

    var modalDelete = document.getElementById('modalCompanyDelete');
    modalDelete.style.display = "none";
}

document.addEventListener("click", function(event) {
    var modal = document.getElementById("myModal");
    var modalEdit = document.getElementById('modal-edit');
    var modalDelete = document.getElementById('modalCompanyDelete');

    if (event.target == modal) {
        modal.style.display = "none";
    }

    if (event.target == modalEdit) {
        modalEdit.style.display = 'none';
    }

    if (event.target == modalDelete) {
        modalDelete.style.display = "none";
    }
});

document.addEventListener("keydown", function(event) {
    var modal = document.getElementById("myModal");
    var modalEdit = document.getElementById('modal-edit');
    var modalDelete = document.getElementById('modalCompanyDelete');

    if (event.keyCode == 27) {
        modal.style.display = "none";
        modalEdit.style.display = 'none';
        modalDelete.style.display = "none";
    }
});

function openEditModal(idCompany, nameEn, nameVn, companyType, industry, abbreviation) {

    var modal = document.getElementById("modal-edit");
    var id = document.getElementById("idCompanyEdit");
    var en = document.getElementById("nameEnEdit");
    var vn = document.getElementById("nameVnEdit");
    var types = document.getElementsByClassName("cb-company-type-edit");
    var industryVar = document.getElementsByClassName('cb-company-industry-edit');
    var abbr = document.getElementById("abbreviationEdit");

    //Reset checkboxes
    for (let i = 0; i < types.length; i++) {
        types[i].checked = false;
    }
    for (let i = 0; i < industryVar.length; i++) {
        industryVar[i].checked = false;
    }

    modal.style.display = 'block';

    id.value = idCompany;
    en.value = nameEn;
    vn.value = nameVn;
    abbr.value = abbreviation;

    const companyTypes = companyType.split(",");
    const set = new Set();
    for (let i = 0; i < companyTypes.length; i++) {
        set.add(companyTypes[i].trim());
    }

    for (let i = 0; i < types.length; i++) {
        let typeVal = types[i].value.trim();
        if (set.has(typeVal)) {
            types[i].checked = true;
        }
    }

    const industryArr = industry.split(",");
    const industrySet = new Set();
    for (let i = 0; i < industryArr.length; i++) {
        industrySet.add(industryArr[i].trim());
    }

    for (let i = 0; i < industryVar.length; i++) {
        let industryVal = industryVar[i].value.trim();
        if (industrySet.has(industryVal)) {
            industryVar[i].click();
        }
    }
}

function openModalConfirmDelete() {
    var modal = document.getElementById('modalCompanyDelete');
    modal.style.display = 'block';
    document.getElementById('inputCompanyDelete').focus();
}
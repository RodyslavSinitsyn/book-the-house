function csrfHeader() {
    return document.querySelector('meta[name="csrf-header"]').content
}

function csrfToken() {
    return document.querySelector('meta[name="csrf-token"]').content
}

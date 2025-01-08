document.addEventListener('DOMContentLoaded', function () {
    const avatar = document.getElementById("header-avatar");
    const dropdownMenu = document.getElementById("header-dropdown-menu");

    avatar.addEventListener("click", () => {
        dropdownMenu.classList.toggle("hidden");
    });

    window.addEventListener("click", (e) => {
        if (!e.target.closest('#header-avatar') && !e.target.closest('#header-dropdown-menu')) {
            dropdownMenu.classList.add("hidden");
        }
    });
});

function showInfoNotification(message, duration, callback) {
    const popup = document.getElementById('info-popup');
    popup.innerText = message;

    // Show the popup
    popup.classList.remove('translate-x-full', 'opacity-0');
    popup.classList.add('translate-x-0', 'opacity-100');

    // Hide the popup after 3 seconds
    setTimeout(() => {
        popup.classList.remove('translate-x-0', 'opacity-100');
        popup.classList.add('translate-x-full', 'opacity-0');
        if (callback) callback()
    }, duration ? duration : 3000);
}
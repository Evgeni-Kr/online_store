document.addEventListener('DOMContentLoaded', function() {
    const menu = document.getElementById('menu');
    const burgerButton = document.querySelector('.catalog-btn');
    const overlay = document.getElementById('burgerMenuOverlay');

    // Проверяем, что элементы существуют
    if (!menu || !burgerButton) {
        console.error('Burger menu elements not found!');
        return;
    }

    burgerButton.addEventListener('click', openMenu);

    // Закрытие по клику на оверлей
    if (overlay) {
        overlay.addEventListener('click', closeMenu);
    }

    // Закрытие по ESC
    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape' && menu.classList.contains('is-open')) {
            closeMenu();
        }
    });

    function openMenu() {
        if (!menu.classList.contains('is-open')) {
            menu.classList.add('is-open');
            burgerButton.classList.add('is-active')
            if (overlay) {
                overlay.classList.add('is-active');
            }
            document.body.classList.add('no-scroll');
        } else {
            closeMenu();
        }
    }

    function closeMenu() {
        menu.classList.remove('is-open');
        burgerButton.classList.remove('is-active')
        if (overlay) {
            overlay.classList.remove('is-active');
        }
        document.body.classList.remove('no-scroll');
    }
});
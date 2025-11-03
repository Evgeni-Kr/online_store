const inCartBtn = document.getElementById("in-cart")
const modal = document.querySelector('.modal')
const overlay = document.getElementById('burgerMenuOverlay')
const modalBtn = document.getElementById('modal-btn')

// Проверка существования элементов
if (inCartBtn && modal) {
    inCartBtn.addEventListener('click', openModal);
}

if (overlay) {
    overlay.addEventListener('click', closeModal)
}

function openModal(e) {
    e.preventDefault();
    modal.classList.add('is-active')
    document.body.classList.add('no-scroll')
    if (overlay) {
        overlay.classList.add('is-active')
    }
}

function closeModal() {
    modal.classList.remove('is-active');
    document.body.classList.remove('no-scroll');
    if (overlay) {
        overlay.classList.remove('is-active');
    }
}

// Закрытие по ESC
document.addEventListener('keydown', function (e) {
    if (e.key === 'Escape' && modal.classList.contains('is-active')) {
        closeModal();
    }
});

if (modalBtn) {
    modalBtn.addEventListener("click", addToCart);
}

function addToCart() {
    // Получаем данные из формы
    const productId = document.getElementById('productId').value;
    const quantity = document.getElementById('quantity').value;

    console.log('Product ID:', productId); // Для отладки
    console.log('Quantity:', quantity); // Для отладки

    // Валидация
    if (!productId) {
        alert('Ошибка: ID товара не найден');
        return;
    }

    if (quantity < 1 || quantity > 100) {
        alert('Количество должно быть от 1 до 100');
        return;
    }

    // Создаем объект для отправки
    const requestData = {
        productId: parseInt(productId),
        quantity: parseInt(quantity)
    };

    console.log('Sending data:', requestData); // Для отладки

    let xhr = new XMLHttpRequest();
    xhr.open("POST", "/api/cart/add", true);

    // ОБЯЗАТЕЛЬНО установите заголовки ДО отправки
    xhr.setRequestHeader("Content-Type", "application/json");

    // Добавляем CSRF токен если используется Spring Security
    const csrfToken = getCsrfToken();
    if (csrfToken) {
        xhr.setRequestHeader("X-CSRF-TOKEN", csrfToken);
    }

    xhr.onload = function () {
        console.log('Response status:', xhr.status); // Для отладки
        console.log('Response:', xhr.response); // Для отладки

        if (xhr.status === 200) {
            try {
                const response = JSON.parse(xhr.response);
                alert('Товар успешно добавлен в корзину!');
                closeModal();

                // Можно обновить интерфейс корзины
                updateCartCounter(response);
            } catch (e) {
                console.error('Error parsing response:', e);
                alert('Ошибка при обработке ответа от сервера');
            }
        } else {
            alert(`Ошибка ${xhr.status}: ${xhr.statusText}`);
        }
    };

    xhr.onerror = function() {
        console.error('XHR error');
        alert("Запрос не удался");
    };

    // ОТПРАВЛЯЕМ данные как JSON строку
    xhr.send(JSON.stringify(requestData));
}

// Функция для получения CSRF токена
function getCsrfToken() {
    const token = document.querySelector('meta[name="_csrf"]')?.getAttribute('content') ||
        document.querySelector('input[name="_csrf"]')?.value;
    console.log('CSRF Token:', token); // Для отладки
    return token;
}

// Функция для обновления счетчика корзины
function updateCartCounter(cartData) {
    console.log('Cart data:', cartData); // Для отладки
    const cartCounter = document.querySelector('.cart-counter');
    if (cartCounter && cartData.totalItems !== undefined) {
        cartCounter.textContent = cartData.totalItems;
    }
}
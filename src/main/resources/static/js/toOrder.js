document.addEventListener('DOMContentLoaded', function() {
    const orderBtn = document.getElementById("order-btn");

    if (orderBtn) {
        console.log("Order button found, adding event listener");
        orderBtn.addEventListener("click", addOrder);
    } else {
        console.warn("Order button not found!");
        setTimeout(() => {
            const btn = document.getElementById("order-btn");
            if (btn) {
                console.log("Order button found after delay");
                btn.addEventListener("click", addOrder);
            }
        }, 100);
    }

    // Функция для получения CSRF токена
    function getCsrfToken() {
        // Пытаемся получить из meta тега (Spring Security с Thymeleaf)
        const csrfMeta = document.querySelector('meta[name="_csrf"]');
        const csrfHeaderMeta = document.querySelector('meta[name="_csrf_header"]');

        if (csrfMeta) {
            return {
                token: csrfMeta.getAttribute('content'),
                header: csrfHeaderMeta ? csrfHeaderMeta.getAttribute('content') : 'X-CSRF-TOKEN'
            };
        }

        // Пытаемся получить из скрытого input (формы)
        const csrfInput = document.querySelector('input[name="_csrf"]');
        if (csrfInput) {
            return {
                token: csrfInput.value,
                header: 'X-CSRF-TOKEN'
            };
        }

        // Пытаемся получить из cookie (если настроено с CookieCsrfTokenRepository)
        const csrfCookie = document.cookie.split('; ')
            .find(row => row.startsWith('XSRF-TOKEN='));

        if (csrfCookie) {
            return {
                token: csrfCookie.split('=')[1],
                header: 'X-XSRF-TOKEN'
            };
        }

        console.warn("CSRF token not found!");
        return null;
    }

    async function addOrder() {
        try {
            console.log("Creating order...");
            orderBtn.disabled = true;
            orderBtn.textContent = "Обработка...";

            // Получаем CSRF токен
            const csrf = getCsrfToken();

            // Формируем заголовки
            const headers = {
                "Content-Type": "application/json"
            };

            // Добавляем CSRF токен если найден
            if (csrf) {
                headers[csrf.header] = csrf.token;
                console.log("CSRF token added:", csrf.token.substring(0, 10) + "...");
            } else {
                console.warn("No CSRF token found, request may fail with 403");
            }

            // Отправляем POST запрос
            const response = await fetch("/api/order/create", {
                method: "POST",
                headers: headers,
                credentials: "include" // важно для cookies
            });

            console.log("Response status:", response.status);

            if (response.ok) {
                const order = await response.json();
                console.log("Order created:", order);

                // Показываем успешное сообщение
                showNotification(`Заказ №${order.id} успешно создан! Сумма: ${order.totalPrice}₽`, "success");

                // Редирект на страницу заказов
                setTimeout(() => {
                    window.location.href = "/order/get";
                }, 1500);

            } else if (response.status === 401) {
                showNotification("Пожалуйста, войдите в систему для оформления заказа", "error");
                setTimeout(() => {
                    window.location.href = "/api/login";
                }, 1500);
            } else if (response.status === 403) {
                // CSRF ошибка
                showNotification("Ошибка безопасности. Обновите страницу и попробуйте снова.", "error");
                console.error("CSRF protection error");

                // Показываем более подробную информацию
                if (csrf) {
                    console.error("CSRF token was present but rejected:", {
                        header: csrf.header,
                        token: csrf.token.substring(0, 20) + "..."
                    });
                }
            } else {
                const errorText = await response.text();
                console.error("Server error:", errorText);
                showNotification(`Ошибка: ${errorText || "Неизвестная ошибка сервера"}`, "error");
            }
        } catch (error) {
            console.error("Error creating order:", error);
            showNotification("Ошибка сети. Пожалуйста, попробуйте еще раз.", "error");
        } finally {
            if (orderBtn) {
                orderBtn.disabled = false;
                orderBtn.textContent = "Оформить заказ";
            }
        }
    }

    function createOrderAlternative() {
        const form = document.createElement('form');
        form.method = 'POST';
        form.action = '/api/order/create';
        form.style.display = 'none';

        const csrfInput = document.querySelector('input[name="_csrf"]');
        if (csrfInput) {
            const csrfClone = csrfInput.cloneNode(true);
            form.appendChild(csrfClone);
        }

        document.body.appendChild(form);
        form.submit();
    }

    function showNotification(message, type) {
        let notificationContainer = document.getElementById("notification-container");
        if (!notificationContainer) {
            notificationContainer = document.createElement("div");
            notificationContainer.id = "notification-container";
            notificationContainer.style.cssText = `
                position: fixed;
                top: 20px;
                right: 20px;
                z-index: 10000;
                max-width: 400px;
            `;
            document.body.appendChild(notificationContainer);
        }

        const notification = document.createElement("div");
        notification.textContent = message;
        notification.style.cssText = `
            padding: 15px 20px;
            margin-bottom: 10px;
            border-radius: 5px;
            color: white;
            font-weight: bold;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
            animation: slideIn 0.3s ease-out;
        `;

        if (type === "success") {
            notification.style.backgroundColor = "#4CAF50";
        } else if (type === "error") {
            notification.style.backgroundColor = "#f44336";
        } else if (type === "warning") {
            notification.style.backgroundColor = "#ff9800";
        }

        notificationContainer.appendChild(notification);

        setTimeout(() => {
            notification.style.animation = "slideOut 0.3s ease-out";
            setTimeout(() => {
                if (notification.parentNode === notificationContainer) {
                    notificationContainer.removeChild(notification);
                }
            }, 300);
        }, 5000);
    }

    if (!document.querySelector('#notification-styles')) {
        const style = document.createElement('style');
        style.id = 'notification-styles';
        style.textContent = `
            @keyframes slideIn {
                from {
                    transform: translateX(100%);
                    opacity: 0;
                }
                to {
                    transform: translateX(0);
                    opacity: 1;
                }
            }
            
            @keyframes slideOut {
                from {
                    transform: translateX(0);
                    opacity: 1;
                }
                to {
                    transform: translateX(100%);
                    opacity: 0;
                }
            }
        `;
        document.head.appendChild(style);
    }
});
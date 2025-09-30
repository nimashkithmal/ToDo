function validateChangePasswordForm() {
    // Get the form field values
    const currentPassword = document.getElementById("currentPassword").value;
    const newPassword = document.getElementById("newPassword").value;
    const confirmPassword = document.getElementById("confirmPassword").value;

    // Clear any previous error messages
    const errorMessages = document.getElementById("error-messages-client");
    const errorList = document.getElementById("error-list");
    errorList.innerHTML = ""; // Clear the list
    errorMessages.style.display = "none"; // Hide error container initially

    let hasError = false;

    // Check if any field is empty
    if (!currentPassword) {
        const errorItem = document.createElement("li");
        errorItem.textContent = "Current password is required.";
        errorList.appendChild(errorItem);
        hasError = true;
    }
    if (!newPassword) {
        const errorItem = document.createElement("li");
        errorItem.textContent = "New password is required.";
        errorList.appendChild(errorItem);
        hasError = true;
    }
    if (!confirmPassword) {
        const errorItem = document.createElement("li");
        errorItem.textContent = "Confirm password is required.";
        errorList.appendChild(errorItem);
        hasError = true;
    }

    // Check if passwords match
    if (newPassword !== confirmPassword) {
        const errorItem = document.createElement("li");
        errorItem.textContent = "New password and confirmation do not match.";
        errorList.appendChild(errorItem);
        hasError = true;
    }

    // Check password length
    if (newPassword && newPassword.length < 6) {
        const errorItem = document.createElement("li");
        errorItem.textContent = "New password must be at least 6 characters long.";
        errorList.appendChild(errorItem);
        hasError = true;
    }

    // Display errors if there are any
    if (hasError) {
        errorMessages.style.display = "block";
        // Stop the form from submitting
        return false;
    }

    // Allow form submission if there are no errors
    return true;
}

// Function to show popup notification
function showPopup(message, type = 'success') {
    // Create popup element
    const popup = document.createElement('div');
    popup.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        padding: 15px 20px;
        border-radius: 5px;
        color: white;
        font-weight: bold;
        z-index: 9999;
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        animation: slideIn 0.3s ease-out;
    `;
    
    if (type === 'success') {
        popup.style.backgroundColor = '#28a745';
    } else {
        popup.style.backgroundColor = '#dc3545';
    }
    
    popup.textContent = message;
    
    // Add CSS animation
    const style = document.createElement('style');
    style.textContent = `
        @keyframes slideIn {
            from { transform: translateX(100%); opacity: 0; }
            to { transform: translateX(0); opacity: 1; }
        }
        @keyframes slideOut {
            from { transform: translateX(0); opacity: 1; }
            to { transform: translateX(100%); opacity: 0; }
        }
    `;
    document.head.appendChild(style);
    
    // Add to page
    document.body.appendChild(popup);
    
    // Auto-hide after 3 seconds
    setTimeout(() => {
        popup.style.animation = 'slideOut 0.3s ease-in';
        setTimeout(() => {
            if (popup.parentNode) {
                popup.parentNode.removeChild(popup);
            }
        }, 300);
    }, 3000);
}

// Check for success message on page load and show popup
document.addEventListener('DOMContentLoaded', function() {
    const successMessage = document.querySelector('.alert-success');
    if (successMessage) {
        const message = successMessage.textContent.trim();
        showPopup(message, 'success');
        
        // Auto-hide the success message after 5 seconds
        setTimeout(() => {
            successMessage.style.transition = 'opacity 0.5s ease-out';
            successMessage.style.opacity = '0';
            setTimeout(() => {
                if (successMessage.parentNode) {
                    successMessage.parentNode.removeChild(successMessage);
                }
            }, 500);
        }, 5000);
    }
    
    // Check for error message on page load and show popup
    const errorMessage = document.querySelector('.alert-danger');
    if (errorMessage) {
        const message = errorMessage.textContent.trim();
        showPopup(message, 'error');
    }
});
// Add this function to show and close the custom pop-up with animation
function showPopup(result) {
    const popup = document.getElementById('customPopup');
    const popupResult = document.getElementById('popupResult');

    popupResult.innerText = `Result: ${result}`;
    popup.style.display = 'block';

    // Trigger reflow to restart the animation
    popup.offsetHeight;

    // Add a class to apply the animation
    popup.classList.add('popup-animate');
}

function closePopup() {
    const popup = document.getElementById('customPopup');
    popup.style.display = 'none';
    popup.classList.remove('popup-animate');
}

async function classifyEmail() {
    const emailContent = document.getElementById('emailInput').value;

    try {
        const response = await fetch('http://127.0.0.1:5000/classify', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ emailContent }),
        });

        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        // Use response.json() directly to parse the JSON content
        const resultObject = await response.json();

        // Access the 'result' property directly
        const result = resultObject.result;

        // Display the result in the custom pop-up
        showPopup(result);

    } catch (error) {
        console.error('Error during fetch:', error);

        // Display a pop-up alert for errors
        window.alert('Error occurred during classification');
    }
}

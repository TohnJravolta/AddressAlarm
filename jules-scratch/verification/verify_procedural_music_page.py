from playwright.sync_api import sync_playwright
import os

def verify_final_page():
    """
    Navigates to the final, single-file index.html and takes a screenshot.
    """
    with sync_playwright() as p:
        browser = p.chromium.launch(headless=True)
        page = browser.new_page()

        # Construct the absolute path to the index.html file
        file_path = os.path.abspath("index.html")

        # Use the file:// protocol to open the local file
        page.goto(f"file://{file_path}")

        # Wait a moment for all elements to load
        page.wait_for_timeout(2000)

        # Take a screenshot of the full page
        screenshot_path = "jules-scratch/verification/final_procedural_music_page.png"
        page.screenshot(path=screenshot_path, full_page=True)

        browser.close()
        print(f"Screenshot saved to {screenshot_path}")

if __name__ == "__main__":
    verify_final_page()
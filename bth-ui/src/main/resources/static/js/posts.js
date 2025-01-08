let currentPage = 0;
let isLoading = false;

// Function to load more posts
async function loadMorePosts() {
    if (isLoading) return; // Prevent multiple clicks while loading
    isLoading = true;
    document.getElementById('loading-indicator').style.display = 'block';

    // Increment the page number
    currentPage++;

    try {
        // Use the fetch API to get the next set of posts
        const response = await fetch(`/posts/load?page=${currentPage}`);

        // Check if the response is OK (status code 200)
        if (response.ok) {
            const postsFragment = await response.text();

            // Append the new posts to the container
            const postsContainer = document.getElementById('posts-container');
            postsContainer.innerHTML += postsFragment;

            // Hide loading indicator and reset isLoading flag
            document.getElementById('loading-indicator').style.display = 'none';
            isLoading = false;
        } else {
            alert('Failed to load more posts.');
        }
    } catch (error) {
        console.error('Error loading posts:', error);
        document.getElementById('loading-indicator').style.display = 'none';
        isLoading = false;
    }
}


// Add event listener to the "Load More" button
document.addEventListener("DOMContentLoaded", function () {
    const loadMoreButton = document.getElementById("load-more-button");
    if (loadMoreButton) {
        loadMoreButton.addEventListener("click", loadMorePosts);
    }

    const clearButton = document.getElementById('clear-filters-button');
    const filterForm = document.getElementById('posts-filter-form');
    clearButton.addEventListener("click", e => {
        filterForm.reset();
        window.location.assign('/posts')
    });
});
<script setup>
function levenshtein(){
    var input = document.getElementById("searchInput").value;
    console.log(value);
}
</script>

<template>
    <div>
        <input type="text" id="searchInput" onkeyup="levenshtein()" placeholder="Search..." />
    </div>
</template>


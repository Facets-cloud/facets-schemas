const elements = document.querySelectorAll('[data-animate]');

// Safari IntersectionObserver fallback
if ('IntersectionObserver' in window) {
  const observer = new IntersectionObserver(entries => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        entry.target.classList.add('visible');
      }
    });
  }, { threshold: 0.2 });

  elements.forEach(el => {
    if (el.getBoundingClientRect().top < window.innerHeight * 0.8) {
      el.classList.add('visible');
    }
    observer.observe(el);
  });
} else {
  elements.forEach(el => el.classList.add('visible'));
}

const popup = document.getElementById("popup");
const popupInner = document.getElementById("popupInner");
const backBtn = document.getElementById("backBtn");
let lastClickedCard = null;

document.querySelectorAll(".know-more1").forEach(btn => {
  btn.addEventListener("click", (e) => {
    const card = e.target.closest(".product-card");
    lastClickedCard = card;

    const fullInfo = e.target.nextElementSibling;
    popupInner.innerHTML = fullInfo.innerHTML;
    popup.style.display = "flex";
    popup.offsetHeight; // Safari repaint fix
    popup.style.zIndex = "9999";
  });
});

backBtn.addEventListener("click", () => {
  popup.style.display = "none";
  if (lastClickedCard) {
    try {
      lastClickedCard.scrollIntoView({ behavior: "smooth", block: "center" });
    } catch (e) {
      lastClickedCard.scrollIntoView(); // Safari fallback
    }
  }
});

window.addEventListener('load', () => {
  document.body.style.display = 'none';
  document.body.offsetHeight;
  document.body.style.display = '';
  // Extra for safaris (if you have multiple popups)
  Array.from(document.querySelectorAll('.popup')).forEach(popup => {
    popup.style.display = 'none';
    popup.offsetHeight;
    popup.style.display = '';
  });
});

document.addEventListener('DOMContentLoaded', (event) => {
    // --- Bouncing Logo ---
    // The bouncing logo is handled by CSS animations.
    const bouncingLogo = document.getElementById('bouncing-logo');
    if (bouncingLogo) {
        console.log('Bouncing logo animation is active via CSS.');
    }

    // --- Smoother Mouse Trail Effect ---
    // The styling is in styles.css. This JS handles the smooth positioning.
    const trails = [];
    const numTrails = 15;
    const trailLag = 1.8;

    for (let i = 0; i < numTrails; i++) {
        let trail = document.createElement('div');
        trail.className = 'trail';
        document.body.appendChild(trail);
        trails.push({ el: trail, x: -20, y: -20 });
    }

    let mouseX = -20;
    let mouseY = -20;

    document.addEventListener('mousemove', function(e) {
        mouseX = e.pageX;
        mouseY = e.pageY;
    });

    function animateTrail() {
        let prevX = mouseX;
        let prevY = mouseY;

        trails.forEach((p, i) => {
            const currentX = p.x;
            const currentY = p.y;

            p.x += (prevX - currentX) / trailLag;
            p.y += (prevY - currentY) / trailLag;

            p.el.style.left = p.x + 'px';
            p.el.style.top = p.y + 'px';
            p.el.style.transform = `scale(${(numTrails - i) / numTrails})`;

            prevX = currentX;
            prevY = currentY;
        });

        requestAnimationFrame(animateTrail);
    }
    animateTrail();

    // --- Retro Chunk Effect (on click) ---
    document.addEventListener('click', function(e) {
        // Create a burst of particles at the click location
        for (let i = 0; i < 15; i++) {
            createChunk(e.clientX, e.clientY);
        }
    });

    function createChunk(x, y) {
        const chunk = document.createElement('div');
        const size = Math.floor(Math.random() * 3 + 2) * 2; // Pixel-style sizes (4, 6, 8px)
        const themeColors = ['#00ff9d', '#ff00c1']; // Neon green and pink from the theme
        const color = themeColors[Math.floor(Math.random() * themeColors.length)];

        chunk.style.position = 'fixed';
        chunk.style.left = `${x}px`;
        chunk.style.top = `${y}px`;
        chunk.style.width = `${size}px`;
        chunk.style.height = `${size}px`;
        chunk.style.background = color;
        chunk.style.boxShadow = `0 0 10px ${color}`; // Glow effect
        chunk.style.pointerEvents = 'none';
        chunk.style.zIndex = '9999';

        document.body.appendChild(chunk);

        const angle = Math.random() * Math.PI * 2;
        const force = Math.random() * 8 + 4;
        let vx = Math.cos(angle) * force;
        let vy = Math.sin(angle) * force;
        const gravity = 0.15;
        const friction = 0.98;
        let life = 0;
        const duration = 60 + Math.random() * 40;

        function animateChunk() {
            life++;
            if (life >= duration) {
                chunk.remove();
                return;
            }

            vx *= friction;
            vy *= friction;
            vy += gravity; // Apply gravity for a more dynamic feel

            chunk.style.left = `${parseFloat(chunk.style.left) + vx}px`;
            chunk.style.top = `${parseFloat(chunk.style.top) + vy}px`;
            chunk.style.opacity = 1 - (life / duration);
            requestAnimationFrame(animateChunk);
        }

        animateChunk();
    }
});
  document.addEventListener('DOMContentLoaded', function () {
    const navItems = document.querySelectorAll('.js_nav');
    const navMenus = document.querySelectorAll('.nav_menu');

    navItems.forEach((item, index) => {
      let timeout;
      item.addEventListener('mouseenter', () => {
        clearTimeout(timeout);
        navMenus[index].classList.add('show');
      });

      item.addEventListener('mouseleave', () => {
        timeout = setTimeout(() => {
          navMenus[index].classList.remove('show');
        }, 100);
      }); 

      navMenus[index].addEventListener('mouseenter', () => {
        clearTimeout(timeout);
      });

      navMenus[index].addEventListener('mouseleave', () => {
        timeout = setTimeout(() => {
          navMenus[index].classList.remove('show');
        }, 100);
      });
    });
  });

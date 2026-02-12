import { createRouter, createWebHistory } from "vue-router";

const routes = [

];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

router.beforeEach((to, from, next) => {
    const user = JSON.parse(localStorage.getItem("user"));

    if (!to.meta.requiresAuth) return next();

    if (!user) return next("/LoginRegister");

    const allowedRoles = to.meta.roles;
    if (allowedRoles && !allowedRoles.includes(user.roleName?.toUpperCase())) {
        alert("Bạn không có quyền truy cập trang này!");
        if (user.roleName?.toUpperCase() === "READER") return next("/booksReader");
        if (user.roleName?.toUpperCase() === "STAFF") return next("/");
        if (user.roleName?.toUpperCase() === "ADMIN") return next("/");
    }

    next();
});

export default router;

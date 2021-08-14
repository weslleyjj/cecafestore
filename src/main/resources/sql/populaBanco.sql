--
-- Data for Name: roles; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.roles VALUES (1, 'PRODUTOR');
INSERT INTO public.roles VALUES (2, 'COMPRADOR');
INSERT INTO public.roles VALUES (3, 'FUNCIONARIO');
INSERT INTO public.roles VALUES (4, 'ADMIN');


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.users VALUES (1, true, '$2a$10$dX8SV.x7DlZZ6aiypG/Kn.gHx0.o5X2E5tiEY3qVw7cYR0mAZlwKy', 'produtor');
INSERT INTO public.users VALUES (2, true, '$2a$10$Ngpq0A0BPUd96iuQ0ApBMuvZWm74GWgd0TOCc3SyhhFXsiTVktrsW', 'comprador');
INSERT INTO public.users VALUES (3, true, '$2a$10$GTxhuOqydE6vG8rZ2xhXpezta93DNpp3xusUXtx162jvoyQIM8GP6', 'funcionario');
INSERT INTO public.users VALUES (4, true, '$2a$10$Lzef7b873kCdqgdCmKN/M.o9jhwAJm6c861eE7Zb/CNXHAg9DwO6i', 'admin');


--
-- Data for Name: users_roles; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.users_roles VALUES (1, 1);
INSERT INTO public.users_roles VALUES (2, 2);
INSERT INTO public.users_roles VALUES (3, 3);
INSERT INTO public.users_roles VALUES (4, 4);


--
-- Data for Name: endereco; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.endereco VALUES (1, 'taborda', '59842-365', 'natal', NULL, 'RN', '65', 'Rua florida');
INSERT INTO public.endereco VALUES (2, 'Camarão', '59162-000', 'Umarizal', NULL, 'RN', '145', 'Rua do campo');


--
-- Data for Name: comprador; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.comprador VALUES (1, '(84) 985412365', '854.736.211-20', 'João', 1, 2);


--
-- Data for Name: funcionario_cecafes; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.funcionario_cecafes VALUES (1, '988.456.258-78', '56468', 'Fernácio Fagundes', 3);


--
-- Data for Name: produtor; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.produtor VALUES (1, '(84) 97745-6213', '987.357.452-98', 'Produtor produtivo', 2, 1);


--
-- Data for Name: produto; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.produto VALUES (1, 'Fruta', '2021-09-14 18:14:57', NULL, 'Maçã', '4,50', 100, 'Quilo', 1);
INSERT INTO public.produto VALUES (2, 'Fruta', '2021-09-15 18:14:57', NULL, 'Banana', '1,50', 500, 'Quilo', 1);
INSERT INTO public.produto VALUES (3, 'Verdura', '2021-08-30 18:14:57', NULL, 'Cebola', '4390,00', 2, 'Tonelada', 1);
INSERT INTO public.produto VALUES (4, 'Legume', '2021-09-01 18:14:57', NULL, 'Macaxeira', '5,28', 760, 'Quilo', 1);
INSERT INTO public.produto VALUES (5, 'Hortaliça', '2021-08-28 18:14:57', NULL, 'Alface', '2,40', 150, 'Quilo', 1);


--
-- Data for Name: produto_cecafes; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.produto_cecafes VALUES (1, 'Fruta', 'Maçã', '4,50', 400, 'Quilo');
INSERT INTO public.produto_cecafes VALUES (2, 'Fruta', 'Banana', '1,50', 500, 'Quilo');
INSERT INTO public.produto_cecafes VALUES (3, 'Verdura', 'Cebola', '4390,00', 2, 'Tonelada');
INSERT INTO public.produto_cecafes VALUES (4, 'Legume', 'Macaxeira', '5,28', 760, 'Quilo');
INSERT INTO public.produto_cecafes VALUES (5, 'Hortaliça', 'Alface', '2,40', 150, 'Quilo');

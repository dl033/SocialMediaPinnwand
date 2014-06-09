package de.elite.itprojekt.client.gui;

import java.sql.Timestamp;
import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.elite.itprojekt.shared.PinnwandVerwaltung;
import de.elite.itprojekt.shared.PinnwandVerwaltungAsync;
import de.elite.itprojekt.shared.bo.Abonnement;
import de.elite.itprojekt.shared.bo.Beitrag;
import de.elite.itprojekt.shared.bo.Kommentar;
import de.elite.itprojekt.shared.bo.Like;
import de.elite.itprojekt.shared.bo.Nutzer;



public class BeitragErstellen {
	
public BeitragErstellen() {
	//Konstruktor des Todes
	
}
	
	private Timestamp aktuellesDatum;
	private static Nutzer nutzer;
	PinnwandVerwaltungAsync service = GWT.create(PinnwandVerwaltung.class); // Proxy aufbauen f�r pinnwandverwaltung
	

	//Nutzerobjekt per ID von Cookie holen
	public void setNutzer(Nutzer nutzer) {
		BeitragErstellen.nutzer = nutzer;
		System.out.println("Nutzerobjekt zu Nutzer mit der ID:" + " " + BeitragErstellen.nutzer.getID() + " " + "gesetzt.");
	}
	
	public void holeNutzer() {
		service.sucheNutzerID(Integer.valueOf(Cookies.getCookie("gp5cookie")), new AsyncCallback<Nutzer>() {
			@Override
			public void onFailure(Throwable caught) {
				System.out.println("Fehler");
			}
			@Override
			public void onSuccess(Nutzer result) {
				setNutzer(result);
			}
		});
	}
	public Nutzer getNutzer() {
		return BeitragErstellen.nutzer;
	}
	
	
	private VerticalPanel vPanel = new VerticalPanel();
	private Label eingeloggterUser;
	private PushButton kommentieren;
	private PushButton bearbeiten;
	private Button loeschen;
	private Label textBeitrag;
	private Label datumsAnzeige;
	private PushButton like;
	private PushButton delike = new PushButton("Delike");
	private Label anzahlLikes;
	private FlexTable beitragsGrid = new FlexTable();
	
	//Neuer Beitrag
	
	private VerticalPanel vPanelAddBeitrag = new VerticalPanel();
	private TextArea tArea = new TextArea();
	private Button addBeitrag;
	
	//Kommentare anlegen
	private TextArea tAreak;
	private Button addKommentar;
	
	//Kommentare ausgeben
	private VerticalPanel vPanelk = new VerticalPanel();
	private Label kommentarNutzer;
	private PushButton bearbeitenk;
	private Button loeschenk;
	private Label textBeitragk;
	private Label datumsAnzeigek;
	FlexTable kommentarFlexTable = new FlexTable();
	
	

	
	
	//F�r neuen Beitraghinzuf�gen
	
	
	
	public void beitragAnzeigenVonAbo(final Beitrag beitrag, final Nutzer nutzer) {
		
		//Widgets f�r AboBeitrag
		
		this.eingeloggterUser = new Label(nutzer.getVorname() + " " + nutzer.getNachname());
		this.textBeitrag = new Label(beitrag.getText());
		this.datumsAnzeige = new Label(beitrag.getErstellZeitpunkt().toString());
		this.kommentieren = new PushButton("Kommentieren");
		this.like = new PushButton("Like");
		this.anzahlLikes = new Label();
		
		//CSS Bezeichner
		this.eingeloggterUser.setStylePrimaryName("NutzerName");
		this.datumsAnzeige.setStylePrimaryName("Date");
		this.textBeitrag.setStylePrimaryName("umBruch");
		
		//Dem FlexTable zuordnen
		
		beitragsGrid.setStyleName("panel flexTable");
		
		beitragsGrid.setWidget(0, 0, eingeloggterUser);
		beitragsGrid.setWidget(0, 2, kommentieren);
		beitragsGrid.setWidget(1, 0, textBeitrag);
		beitragsGrid.setWidget(2, 0, datumsAnzeige);
		beitragsGrid.setWidget(2, 2, like);
		beitragsGrid.setWidget(2, 3, anzahlLikes);
		
		
		
		
		
		
		//Likes des jeweiligen Beitrags anzeigen
		
		
		service.likeZaehlen(beitrag, new AsyncCallback<Integer>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(Integer result) {
				anzahlLikes.setText(result.toString());
				
			}
			
		});

		// Fremden Beitrag kommentieren
		
		kommentieren.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				
				addKommentar = new Button("Hinzufuegen");
				
				tAreak = new TextArea();
				tAreak.setVisibleLines(2);
				tAreak.setPixelSize(200, 100);
				
				beitragsGrid.setWidget(4, 0, tAreak);
				beitragsGrid.setWidget(5, 0, addKommentar);
				
				//Bei Klick in das TextAreafeld wird der aktuelle Nutzer geholt
				tAreak.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						holeNutzer();
						
					}
					
				});
					
					//Kommentar speichern
					addKommentar.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							if (tAreak.getValue().isEmpty()) {
								Window.alert("Bitte Text eingeben!");
							}
							else {
								final Kommentar kommentar = new Kommentar();
								kommentar.setText(tAreak.getText());
								kommentar.setErstellZeitpunkt(aktuellesDatum = new Timestamp(System.currentTimeMillis()));
								kommentar.setNutzer(getNutzer());
								kommentar.setBeitrag(beitrag);
								
								System.out.println(kommentar.getNutzerId());
								System.out.println(kommentar.getBeitrag().getText());
								
								service.kommentarErstellen(kommentar, new AsyncCallback<Void>() {

									@Override
									public void onFailure(Throwable caught) {
										System.out.println("War wohl n fehler");
										
									}

									@Override
									public void onSuccess(Void result) {
										tAreak.setVisible(false);
										addKommentar.setVisible(false);
										
										KommentareAuslesen(beitrag);
										
									}
									
								});
		
							}
							
						}
						
					});
			}
			
		});
		
		
		//DAVOR ERST �BERPR�FEN OB SCHON GELIKED IST VON DEM EINGELOGGTEN NUTZER!
		
		
		
		service.likeCheck(getNutzer(), beitrag, new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(Boolean result) {

				
				if (result == true) {
					
					System.out.println("Der Nutzer" + " " + getNutzer().getVorname() + " " + "will den Beitrag" + " " + beitrag.getText() + " " + "Liken");
					System.out.println("Hat es das schon? :" + " " + result);
					
					beitragsGrid.setWidget(2, 2, delike);
					
					delike.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {

							service.einzelnesLikeLoeschen(beitrag, getNutzer(), new AsyncCallback<Void>() {

								@Override
								public void onFailure(Throwable caught) {
									// TODO Auto-generated method stub
									
								}

								@Override
								public void onSuccess(Void result) {
									service.likeZaehlen(beitrag, new AsyncCallback<Integer>() {

										@Override
										public void onFailure(Throwable caught) {
											// TODO Auto-generated method stub
											
										}

										@Override
										public void onSuccess(Integer result) {
											anzahlLikes.setText(result.toString());
											
											NutzerLogin nl = new NutzerLogin();
											nl.refreshBeitraege();
											
										}
										
									});
									
								}
								
							});
							
						}
						
					});
					
					
				} 
				else {


					//Liken
					like.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							
							Like lke = new Like();
							lke.setNutzerId(getNutzer().getID());		
							lke.setNutzer(getNutzer());
							lke.setErstellZeitpunkt(aktuellesDatum = new Timestamp(System.currentTimeMillis()));
							lke.setPinnwandId(getNutzer().getID());
							
							
							service.likeAnlegen(lke, beitrag, new AsyncCallback<Void>() {

								@Override
								public void onFailure(Throwable caught) {
									// TODO Auto-generated method stub
									
								}

								@Override
								public void onSuccess(Void result) {
									beitragsGrid.setWidget(2, 2, delike);
									
									
									service.likeZaehlen(beitrag, new AsyncCallback<Integer>() {

										@Override
										public void onFailure(Throwable caught) {
											// TODO Auto-generated method stub
											
										}

										@Override
										public void onSuccess(Integer result) {
											anzahlLikes.setText(result.toString());
											
											NutzerLogin nl = new NutzerLogin();
											nl.refreshBeitraege();
											
										}
										
									});
									
									
									

									
								}
								
							});
							
						}
						
					});
					

					
				}
				
				
				
			}
			
		});
			
	
		
		this.vPanel.add(beitragsGrid);
		RootPanel.get("Beitrag").add(vPanel);
		
		
		
	}
	
	//Fremde Kommentare kann man nicht bearbeiten oder l�schen
	
	public void fremdeKommentareAuslesen(Beitrag beitrag) {

		int id = beitrag.getID();

		service.findeAlleKommentare(id, new AsyncCallback<ArrayList<Kommentar>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ArrayList<Kommentar> result) {
				for (final Kommentar k : result) {

					
					kommentarNutzer = new Label(k.getNutzer().getVorname() + " " + k.getNutzer().getNachname());
					textBeitragk = new Label(k.getText());
					datumsAnzeigek = new Label(k.getErstellZeitpunkt().toString());

					kommentarNutzer.setStylePrimaryName("NutzerName");
					datumsAnzeigek.setStylePrimaryName("Date");
					textBeitragk.setStylePrimaryName("umBruch");
					
					kommentarFlexTable.setStylePrimaryName("Kommentar");
					
					//Dem FlexTable zuordnen
					
					kommentarFlexTable.setWidget(0, 0, kommentarNutzer);
					kommentarFlexTable.setWidget(1, 0, textBeitragk);
					kommentarFlexTable.setWidget(2, 0, datumsAnzeigek);

					
					vPanelk.add(kommentarFlexTable);

					vPanel.add(vPanelk);
	
				}
				
			}
			
		});

	}
	
	
	//Ende Fremde kommentare Anzeigen
	
	
	
	

	public void beitragAnzeigen(final Beitrag beitrag, final Nutzer nutzer) {
		
		//Widgets erzeugen f�r Beitrag

		this.eingeloggterUser = new Label(nutzer.getVorname() + " " + nutzer.getNachname());
		this.kommentieren = new PushButton("Kommentieren");
		this.bearbeiten = new PushButton("Bearbeiten");
		this.loeschen = new Button();
		this.textBeitrag = new Label(beitrag.getText());
		this.datumsAnzeige = new Label(beitrag.getErstellZeitpunkt().toString());
		this.like = new PushButton("Like");
		this.anzahlLikes = new Label();
		
		//CSS Bezeichner
		this.loeschen.setStylePrimaryName("Loeschen");
		this.eingeloggterUser.setStylePrimaryName("NutzerName");
		this.datumsAnzeige.setStylePrimaryName("Date");
		this.textBeitrag.setStylePrimaryName("umBruch");
		
		beitragsGrid.setStyleName("panel flexTable");
		
		beitragsGrid.setWidget(0, 0, eingeloggterUser);
		beitragsGrid.setWidget(0, 2, kommentieren);
		beitragsGrid.setWidget(0, 3, bearbeiten);
		beitragsGrid.setWidget(0, 4, loeschen);
		beitragsGrid.setWidget(1, 0, textBeitrag);
		beitragsGrid.setWidget(2, 0, datumsAnzeige);
		beitragsGrid.setWidget(2, 3, like);
		beitragsGrid.setWidget(2, 4, anzahlLikes);
		
		//Likes des Beitrags anzeigen
		
		service.likeZaehlen(beitrag, new AsyncCallback<Integer>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(Integer result) {
				anzahlLikes.setText(result.toString());
				
			}
			
		});
		
	
		this.vPanel.add(beitragsGrid);
		RootPanel.get("Beitrag").add(vPanel);
		
		//ClickHandler m�ssen f�r jedes Beitragobjekt gelten, darum m�ssen sie hier definiert werden
		
		//Beitrag l�schen
		
		loeschen.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				service.textBeitragLoeschen(beitrag, new AsyncCallback<Void>() {
							@Override
							public void onFailure(Throwable caught) {
								Window.alert("Fehler beim loeschen!");
							}

							@Override
							public void onSuccess(Void result) {
								Window.alert("Textbeitrag wurde geloescht!");
								beitragsGrid.removeFromParent();
								kommentarFlexTable.removeFromParent();
							}
						});
			}

		});
		
		//Beitrag bearbeiten
		
		bearbeiten.addClickHandler(new ClickHandler () {
			@Override
			public void onClick(ClickEvent event) {
				
				final Button speichern = new Button("Fertig");
				final TextBox newBeitrag = new TextBox();
				newBeitrag.setText(textBeitrag.getText());
				beitragsGrid.setWidget(1, 0, newBeitrag);
				beitragsGrid.setWidget(1, 1, speichern);
				
					//Bearbeiteter Text Speichern und in Label zur�ckverwandeln (*Magic*)
						speichern.addClickHandler(new ClickHandler() {
							@Override
							public void onClick(ClickEvent event) {
								// TODO Auto-generated method stub
								beitrag.setText(newBeitrag.getText());
								beitrag.setErstellZeitpunkt(aktuellesDatum = new Timestamp(System.currentTimeMillis()));

								service.textBeitragBearbeiten(beitrag, new AsyncCallback<Beitrag>() {

									@Override
									public void onFailure(Throwable caught) {
										// TODO Auto-generated method stub
										
									}

									@Override
									public void onSuccess(Beitrag result) {
										// TODO Auto-generated method stub
										Label beitrag = new Label(newBeitrag.getText());
										beitrag.setStylePrimaryName("umBruch");
										
										beitragsGrid.setWidget(1, 0, beitrag);
										speichern.setVisible(false);
									}
									
								});
							}
						});
			}
		});
		
		//Kommentar hinzuf�gen
		
		kommentieren.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				
				addKommentar = new Button("Hinzufuegen");
				
				tAreak = new TextArea();
				tAreak.setVisibleLines(2);
				tAreak.setPixelSize(200, 100);
				
				beitragsGrid.setWidget(4, 0, tAreak);
				beitragsGrid.setWidget(5, 0, addKommentar);
					
					//Kommentar speichern
					addKommentar.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							if (tAreak.getValue().isEmpty()) {
								Window.alert("Bitte Text eingeben!");
							}
							else {
								final Kommentar kommentar = new Kommentar();
								kommentar.setText(tAreak.getText());
								kommentar.setErstellZeitpunkt(aktuellesDatum = new Timestamp(System.currentTimeMillis()));
								kommentar.setNutzer(nutzer);
								kommentar.setBeitrag(beitrag);
								
								System.out.println(kommentar.getBeitrag().getText());
								
								service.kommentarErstellen(kommentar, new AsyncCallback<Void>() {

									@Override
									public void onFailure(Throwable caught) {
										System.out.println("War wohl n fehler");
										
									}

									@Override
									public void onSuccess(Void result) {
										tAreak.setVisible(false);
										addKommentar.setVisible(false);
										
										KommentareAuslesen(beitrag);
										
									}
									
								});
		
								
							}
							
						}
						
					});
			}
			
		});
		
		//DAVOR ERST �BERPR�FEN OB SCHON GELIKED IST VON DEM EINGELOGGTEN NUTZER!
		
		
		
		service.likeCheck(getNutzer(), beitrag, new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(Boolean result) {

				
				if (result == true) {
					
					beitragsGrid.setWidget(2, 3, delike);
					
					delike.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {

							service.einzelnesLikeLoeschen(beitrag, getNutzer(), new AsyncCallback<Void>() {
								
								@Override
								public void onFailure(Throwable caught) {
									// TODO Auto-generated method stub
									
								}

								@Override
								public void onSuccess(Void result) {
									service.likeZaehlen(beitrag, new AsyncCallback<Integer>() {

										@Override
										public void onFailure(Throwable caught) {
											// TODO Auto-generated method stub
											
										}

										@Override
										public void onSuccess(Integer result) {
											anzahlLikes.setText(result.toString());
											NutzerLogin nl = new NutzerLogin();
											nl.refreshBeitraege();
											
										}
										
									});
									
								}
								
							});
							
						}
						
					});
					
					
				} 
				else {


					//Liken
					like.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							
							Like lke = new Like();
							lke.setNutzerId(nutzer.getID());		
							lke.setNutzer(nutzer);
							lke.setErstellZeitpunkt(aktuellesDatum = new Timestamp(System.currentTimeMillis()));
							lke.setPinnwandId(nutzer.getID());
							
							
							service.likeAnlegen(lke, beitrag, new AsyncCallback<Void>() {

								@Override
								public void onFailure(Throwable caught) {
									// TODO Auto-generated method stub
									
								}

								@Override
								public void onSuccess(Void result) {
									beitragsGrid.setWidget(2, 3, delike);
									
									
									service.likeZaehlen(beitrag, new AsyncCallback<Integer>() {

										@Override
										public void onFailure(Throwable caught) {
											// TODO Auto-generated method stub
											
										}

										@Override
										public void onSuccess(Integer result) {
											anzahlLikes.setText(result.toString());
											NutzerLogin nl = new NutzerLogin();
											nl.refreshBeitraege();
											
										}
										
									});
									
									
									

									
								}
								
							});
							
						}
						
					});
					

					
				}
				
				
				
			}
			
		});
	
		
	
		

		
		
		
		
	}

	
	//Eigene Kommentare werden anhand des aktuell eingeloggten Nutzers angezeigt.
	//Wenn ein Kommentar nicht von dem aktuell eingeloggten Nutzer stammt, dann �ndert sich die darstellung.
	//Denn dann kann man die Kommentare nicht bearbeiten und l�schen.
	public void KommentareAuslesen(final Beitrag beitrag) {
		

		int id = beitrag.getID();

		service.findeAlleKommentare(id, new AsyncCallback<ArrayList<Kommentar>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ArrayList<Kommentar> result) {
				for (final Kommentar k : result) {

					if (getNutzer().getID() == k.getNutzer().getID()) {

					
					kommentarNutzer = new Label(k.getNutzer().getVorname() + " " + k.getNutzer().getNachname());
					bearbeitenk = new PushButton("Bearbeiten");
					textBeitragk = new Label(k.getText());
					datumsAnzeigek = new Label(k.getErstellZeitpunkt().toString());
					loeschenk = new Button();
					
					//Design
					//kommentarFlexTable.addStyleName("Kommentar");
					loeschenk.setStylePrimaryName("Loeschen");
					kommentarNutzer.setStylePrimaryName("NutzerName");
					datumsAnzeigek.setStylePrimaryName("Date");
					textBeitragk.setStylePrimaryName("umBruch");
					
					kommentarFlexTable.setStylePrimaryName("Kommentar");
					
					//Dem FlexTable zuordnen
					
					kommentarFlexTable.setWidget(0, 0, kommentarNutzer);
					kommentarFlexTable.setWidget(0, 1, bearbeitenk);
					kommentarFlexTable.setWidget(0, 2, loeschenk);
					kommentarFlexTable.setWidget(1, 0, textBeitragk);
					kommentarFlexTable.setWidget(2, 0, datumsAnzeigek);

					
					vPanelk.add(kommentarFlexTable);

					vPanel.add(vPanelk);
					

					
					//Kommentar loeschen
					loeschenk.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							
							service.kommentarLoeschen(k, new AsyncCallback<Void>() {
								@Override
								public void onFailure(Throwable caught) {
									Window.alert("Fehler beim loeschen!");
								}

								@Override
								public void onSuccess(Void result) {
									Window.alert("Kommentar wurde geloescht!");
									kommentarFlexTable.removeFromParent();
								}
							});
							
						}
						
					});
					
					//Kommentar bearbeiten
					bearbeitenk.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {


							
							final Button speichern = new Button("Fertig");
							final TextBox newKommentar = new TextBox();
							newKommentar.setText(k.getText());
							kommentarFlexTable.setWidget(1, 0, newKommentar);
							kommentarFlexTable.setWidget(1, 1, speichern);
								//Bearbeiteter Text Speichern und in Label zur�ckverwandeln (*Magic*)
									speichern.addClickHandler(new ClickHandler() {
										@Override
										public void onClick(ClickEvent event) {
											// TODO Auto-generated method stub
											k.setText(newKommentar.getText());
											k.setErstellZeitpunkt(aktuellesDatum = new Timestamp(System.currentTimeMillis()));
											service.kommentarBearbeiten(k, new AsyncCallback<Kommentar>() {

												@Override
												public void onFailure(Throwable caught) {
													// TODO Auto-generated method stub
													
												}

												@Override
												public void onSuccess(Kommentar result) {
													// TODO Auto-generated method stub
													Label kommentar = new Label(newKommentar.getText());
													kommentar.setStylePrimaryName("umBruch");
													
													kommentarFlexTable.setWidget(1, 0, kommentar);
													speichern.setVisible(false);
												}
												
											});
										}
									});
								}
						
							});
					}
					else {
						fremdeKommentareAuslesen(beitrag);
					}

				}
				
			}
			
		});

	}
	//Abobeitr�ge anzeigen
	public void abonnementBeitraegeAnzeigen(Nutzer nutzer) {
		
		
		
	//Ersmal alle abo's rausziehen
		
		int id = nutzer.getID();
		
		service.zeigeAlleAbosPerNutzer(id, new AsyncCallback<ArrayList<Abonnement>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				System.out.println("Error in Navigation!");
			}

			@Override
			public void onSuccess(ArrayList<Abonnement> result) {
					
					for (Abonnement abo :result) {
						getBeitraeVonAbo(abo);
					}
				}

		});
	}
	//Dann die Beitr�ge von den Abonnenten rausziehen
	public void getBeitraeVonAbo(final Abonnement abo) {
		
		int id = abo.getPinnwand().getNutzer().getID();

		
		service.sucheBeitragPerPinnwand(id, new AsyncCallback<ArrayList<Beitrag>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ArrayList<Beitrag> result) {
				
				for (Beitrag b: result) {
					System.out.println("");
					System.out.println("Er schrieb:");
					System.out.println(abo.getPinnwand().getNutzer().getVorname());
					System.out.println(b.getText());
					
					BeitragErstellen erstelle = new BeitragErstellen();
					erstelle.beitragAnzeigenVonAbo(b,abo.getPinnwand().getNutzer());
					
					erstelle.KommentareAuslesen(b);
				}
				
			}
			
		});
		
		
	}


	//END OF ABOBEITR�GE
	
	
	public void beitragHinzufuegen() {
		
		tArea.setVisibleLines(2);
		tArea.setPixelSize(473, 15);
		this.addBeitrag = new Button("Hinzufuegen");
		
		//ClickHandler f�r neuen Beitrag
		
		this.addBeitrag.addClickHandler(new addBeitragClickHandler());
		this.tArea.addClickHandler(new getNutzerClickHandler());
		
		
		this.vPanelAddBeitrag.add(tArea);
		this.vPanelAddBeitrag.add(addBeitrag);
		
		RootPanel.get("neuer_Beitrag").add(vPanelAddBeitrag);
	}
	
	
	
	
	
	//ClickHandler der den Beitrag hinzuf�gt
	private class addBeitragClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			
			if (tArea.getText().isEmpty()) {
				Window.alert("Bitte Text eingeben!");
			}
			else {
			addBeitragAsync(getNutzer(), tArea.getText());
			tArea.setText(null);
			}
		}
	}
	//ClickHandler muss da sein, damit das Nutzer objekt geholt wird.
	
	private class getNutzerClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			holeNutzer();
		}
	}
	
	
	public void addBeitragAsync(Nutzer nutzer, String textBeitrag) {
		
		Beitrag beitrag = new Beitrag();
		beitrag.setNutzerId(nutzer.getID());
		beitrag.setText(textBeitrag);
		beitrag.setErstellZeitpunkt(aktuellesDatum = new Timestamp(System.currentTimeMillis()));
		

		service.textBeitragErstellen(beitrag, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				System.out.println("War wohl n fehler");
				
			}

			@Override
			public void onSuccess(Void result) {
				zeigeAlleBeitraege(getNutzer());
			}
			
		});
	}
	
	public void zeigeAlleBeitraege(Nutzer nutzer) {
		
		RootPanel.get("Beitrag").clear();
		int id = nutzer.getID();
		final Nutzer n = nutzer;

		//Lie�t fremde Kommentare aus
		abonnementBeitraegeAnzeigen(n);
		
		service.findeAlleUserBeitraege(id, new AsyncCallback<ArrayList<Beitrag>>() {

			@Override
			public void onFailure(Throwable caught) {
				System.out.println("hmm2");
				
			}

			@Override
			public void onSuccess(ArrayList<Beitrag> result) {
				
				for (Beitrag b : result) {
					BeitragErstellen erstelle = new BeitragErstellen();
					erstelle.beitragAnzeigen(b,n);
					erstelle.KommentareAuslesen(b);
				}
				
			}
			
		});
		
	}
	
}
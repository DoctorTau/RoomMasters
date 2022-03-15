using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using Assets.Scripts;

public class ObjectsMenu : MonoBehaviour
{
    public GameObject ScrollView;
    public GameObject FloarObject;

    private ScrollRect scroll;
    void Start()
    {
        scroll = ScrollView.GetComponent<ScrollRect>();
        Floor floor = FloarObject.GetComponent<Floor>();
        addMenuItem(ObjectsMenuButtonFabric.Create("Furniture/PreviewPics/Table", "Furniture/Objects/TableObj", floor, "Table"));
        addMenuItem(ObjectsMenuButtonFabric.Create("Furniture/PreviewPics/Bed", "Furniture/Objects/BedObj", floor, "Bed"));
        addMenuItem(ObjectsMenuButtonFabric.Create("Furniture/PreviewPics/Chair", "Furniture/Objects/ChairObj", floor, "Chair"));
        addMenuItem(ObjectsMenuButtonFabric.Create("Furniture/PreviewPics/Closet", "Furniture/Objects/ClosetObj", floor, "Closet"));
        addMenuItem(ObjectsMenuButtonFabric.Create("Furniture/PreviewPics/WaterCooler", "Furniture/Objects/WaterCoolerObj", floor, "WaterCooler"));
    }

    // Update is called once per frame
    void Update()
    {

    }

    public void addMenuItem(GameObject menuItem)
    {
        RectTransform content = scroll.content;
        RectTransform itemRT = menuItem.GetComponent<RectTransform>();
        itemRT.SetParent(content);
        itemRT.localScale = Vector3.one;
        itemRT.position = Vector3.zero;
        itemRT.localPosition = Vector3.zero;
        itemRT.anchoredPosition = Vector3.zero;
    }
}

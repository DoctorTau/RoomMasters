using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class Floar : MonoBehaviour
{
    public Vector2Int GridSize = new Vector2Int(10, 10);
    public List<GameObject> buttons;

    private GameObject[,] grid;
    private GameObject selectedObject;
    private Camera mainCamera;
    private float deltaTime = 0.3f;
    bool dragging = false;

    private void Awake()
    {
        grid = new GameObject[GridSize.x, GridSize.y];
        mainCamera = Camera.main;
        foreach (var button in buttons) button.SetActive(false);
    }

    public void CreateNewObject(GameObject furniturePrehub)
    {
        if (selectedObject != null)
            Destroy(selectedObject);

        selectedObject = Instantiate(furniturePrehub);
    }
    public void CancelSelection()
    {
        selectedObject.GetComponent<Outline>().OutlineMode = Outline.Mode.OutlineHidden;
        selectedObject = null;
        foreach (var button in buttons) button.SetActive(false);
    }

    private void SelectObject(GameObject obj)
    {
        foreach (var button in buttons) button.SetActive(true);
        selectedObject = obj;
        var selection = selectedObject.GetComponent<Outline>();
        selection.OutlineMode = Outline.Mode.OutlineVisible;
    }



    void Update()
    {
        if (selectedObject != null)
        {
            var selectedObjectSizeX = selectedObject.GetComponent<Furniture>().Size.x;
            var selectedObjectSizeY = selectedObject.GetComponent<Furniture>().Size.y;
            if (Input.touchCount > 0 && (Input.GetTouch(0).phase == TouchPhase.Began
                || Input.GetTouch(0).phase == TouchPhase.Moved))
            {
                Touch touch = Input.touches[0];
                Vector3 positionOfTouch = touch.position;

                var groundPlane = new Plane(Vector3.up, Vector3.zero);

                Ray ray = mainCamera.ScreenPointToRay(positionOfTouch);
                {
                    if (groundPlane.Raycast(ray, out float position))
                    {
                        Vector3 worldPosition =
                            ((Ray)mainCamera.ScreenPointToRay(positionOfTouch)).GetPoint(position);
                        worldPosition.x = Mathf.RoundToInt(worldPosition.x);
                        worldPosition.z = Mathf.RoundToInt(worldPosition.z);
                        if (worldPosition.x - selectedObjectSizeX < GridSize.x - 1
                            && worldPosition.z - selectedObjectSizeY < GridSize.y - 1
                            && worldPosition.x >= 0 && worldPosition.z >= 0)

                            selectedObject.transform.position = worldPosition;
                    }
                }
            }
        }

        else
        {
            if (Input.touchCount > 0 && Input.GetTouch(0).phase == TouchPhase.Stationary)
            {
                Touch touch = Input.GetTouch(0);
                Ray ray = mainCamera.ScreenPointToRay(touch.position);
                if (Physics.Raycast(ray, out RaycastHit hit))
                {
                    if (hit.collider.gameObject.tag == "FloarFurniture")
                    {
                        if (deltaTime > 0) deltaTime -= Time.deltaTime;
                        else
                        {
                            SelectObject(hit.collider.gameObject);
                            deltaTime = 0.3f;
                        }
                    }
                }
            }
        }




    }

}

